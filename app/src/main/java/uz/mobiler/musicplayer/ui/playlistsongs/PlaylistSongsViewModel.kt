package uz.mobiler.musicplayer.ui.playlistsongs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.repository.SongRepository
import javax.inject.Inject

@HiltViewModel
class PlaylistSongsViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _playlistSongs = MutableLiveData<List<Song>>()
    val playlistSongs: MutableLiveData<List<Song>> get() = _playlistSongs

    fun getPlaylistSongs(playlistId: Long) {
        _playlistSongs.value = songRepository.getPlaylistSongs(playlistId)
    }

    fun removeSongFromPlaylist(playlistId: Long, song: Song) {
        songRepository.removeSongFromPlaylist(playlistId, song)
        getPlaylistSongs(playlistId)
    }

}