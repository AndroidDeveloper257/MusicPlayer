package uz.alimov.musicplayer.presentation.screen.home

import uz.alimov.musicplayer.domain.all_songs.model.Song

data class HomeState(
    val songList: List<Song> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val showRationale: Boolean = false,
    val showOpenSettingsDialog: Boolean = false
) {
    val isEmpty get() = songList.isEmpty()
}