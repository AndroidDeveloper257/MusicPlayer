package uz.alimov.musicplayer.presentation.util

import android.Manifest
import android.os.Build

object PermissionUtils {
    val mediaPermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_AUDIO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE
}