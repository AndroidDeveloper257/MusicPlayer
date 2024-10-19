package uz.mobiler.musicplayer.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.repository.SongRepository
import uz.mobiler.musicplayer.utils.NavigationOptions
import javax.inject.Inject

@HiltViewModel
class SharedMusicViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _currentSong = MutableLiveData<Song?>()
    val currentSong get() = _currentSong

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying get() = _isPlaying

    private var currentPlaylist: List<Song> = listOf()
    private var navigationOption: NavigationOptions = NavigationOptions.HOME
    private var playlistId: Long? = null

    fun setNavigationContext(
        options: NavigationOptions,
        playlistId: Long? = null
    ) {
        navigationOption = options
        this.playlistId = playlistId
//        loadPlayList()
    }

//    private fun loadPlayList() {
//        viewModelScope.launch {
//            currentPlaylist = when (navigationOption) {
//                NavigationOptions.HOME -> songRepository.getAllSongs()
//                NavigationOptions.FAVORITE -> songRepository.getFavoriteSongs()
//            }
//        }
//    }

}