package uz.alimov.musicplayer.presentation.screen.home

sealed class HomeEffect {
    object RequestPermission: HomeEffect()
    object OpenSettings: HomeEffect()
    object PermissionDenied: HomeEffect()
}