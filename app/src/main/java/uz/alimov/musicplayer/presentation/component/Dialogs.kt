package uz.alimov.musicplayer.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun RationaleDialog(
    message: String,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 30.dp,
                        vertical = 20.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Permission is needed",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onConfirm
                    ) {
                        Text(
                            text = "Confirm",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun OpenSettingsDialog(
    onSettingsClick: () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true,
            decorFitsSystemWindows = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Permission denied. Please enable it in settings",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onSettingsClick
                    ) {
                        Text(text = "Open settings", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}