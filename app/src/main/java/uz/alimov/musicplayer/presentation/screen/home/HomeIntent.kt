package uz.alimov.musicplayer.presentation.screen.home

sealed class HomeIntent {
    data class OnPermissionRequestResult(val isGranted: Boolean): HomeIntent()
    object OnShowRationale: HomeIntent()
    object OnShowOpenSettings: HomeIntent()
    object OnRationaleConfirmed: HomeIntent()
    object OnOpenSettingsConfirmed: HomeIntent()
    object HideError: HomeIntent()
}