package uz.alimov.musicplayer.presentation.screen.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import uz.alimov.musicplayer.presentation.component.OpenSettingsDialog
import uz.alimov.musicplayer.presentation.component.RationaleDialog
import uz.alimov.musicplayer.presentation.ui.theme.MusicPlayerTheme
import uz.alimov.musicplayer.presentation.util.PermissionUtils.mediaPermission

@ExperimentalPermissionsApi
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onIntent(HomeIntent.OnPermissionRequestResult(isGranted))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.OpenSettings -> {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }

                HomeEffect.PermissionDenied -> {
                    if (
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity,
                            mediaPermission
                        )
                    ) {
                        viewModel.onIntent(HomeIntent.OnShowRationale)
                    } else {
                        viewModel.onIntent(HomeIntent.OnShowOpenSettings)
                    }
                }

                HomeEffect.RequestPermission -> {
                    requestPermissionLauncher.launch(mediaPermission)
                }
            }
        }
    }

    LaunchedEffect(state.value.error) {
        state.value.error?.let {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(it)
                viewModel.onIntent(HomeIntent.HideError)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        }
    ) { padding ->
        if (state.value.showRationale) {
            RationaleDialog(
                message = "Permission is needed to access songs",
                onConfirm = {
                    viewModel.onIntent(HomeIntent.OnRationaleConfirmed)
                }
            )
        }

        if (state.value.showOpenSettingsDialog) {
            OpenSettingsDialog {
                viewModel.onIntent(HomeIntent.OnOpenSettingsConfirmed)
            }
        }

        if (!state.value.isEmpty) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = state.value.songList,
                    key = { it.id }
                ) { song ->
                    Text(
                        text = song.title
                    )
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@Preview
@Composable
private fun HomeScreenPreview() {
    MusicPlayerTheme {
        HomeScreen()
    }
}