package uz.mobiler.musicplayer.ui.all

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.repository.SongRepository
import javax.inject.Inject

@HiltViewModel
class AllSongsViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _allSongs = MutableLiveData<List<Song>>()
    val allSongs get() = _allSongs

    fun getAllSongs() {
        viewModelScope.launch {
            _allSongs.value = songRepository.getAllSongs()
        }
    }

}