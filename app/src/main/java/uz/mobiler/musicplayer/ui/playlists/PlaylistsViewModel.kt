package uz.mobiler.musicplayer.ui.playlists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mobiler.musicplayer.database.entity.PlaylistEntity
import uz.mobiler.musicplayer.repository.SongRepository
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _playlists = MutableLiveData<List<PlaylistEntity>>()
    val playlists get() = _playlists

    init {
        fetchPlaylists()
    }

    private fun fetchPlaylists() {
        _playlists.value = songRepository.getPlaylists()
    }

    fun addPlaylist(playlist: PlaylistEntity) {
        songRepository.insertPlaylist(playlist)
        fetchPlaylists()
    }

    fun removePlaylist(playlist: PlaylistEntity) {
        songRepository.removePlaylist(playlist)
        fetchPlaylists()
    }

}