package uz.mobiler.musicplayer.ui.recent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.repository.SongRepository
import javax.inject.Inject

@HiltViewModel
class RecentViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _recentSongs = MutableLiveData<List<Song>>()
    val recentSongs get() = _recentSongs

    init {
        fetchRecentSongs()
    }

    private fun fetchRecentSongs() {
        _recentSongs.value = songRepository.getRecentSongs()
    }

}