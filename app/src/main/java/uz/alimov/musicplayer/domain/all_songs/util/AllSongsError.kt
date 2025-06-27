package uz.alimov.musicplayer.domain.all_songs.util

sealed class AllSongsError {
    object PermissionDenied: AllSongsError()
    data class UnknownError(val message: String): AllSongsError()
}