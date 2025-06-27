@file:OptIn(ExperimentalPermissionsApi::class)

package uz.alimov.musicplayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import uz.alimov.musicplayer.presentation.navigation.MusicNavGraph
import uz.alimov.musicplayer.presentation.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            MusicPlayerTheme {
                val navController = rememberNavController()
                MusicNavGraph(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicPlayerTheme {
        MusicNavGraph()
    }
}