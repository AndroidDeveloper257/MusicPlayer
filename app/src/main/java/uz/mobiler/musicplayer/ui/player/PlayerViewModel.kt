package uz.mobiler.musicplayer.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.repository.SongRepository
import uz.mobiler.musicplayer.utils.NavigationOptions
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _currentSong = MutableLiveData<Song>()
    val currentSong get() = _currentSong

    private val _songList = MutableLiveData<List<Song>>()
    val songList get() = _songList

    private val _index = MutableLiveData<Int>()
    val index get() = _index

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying get() = _isPlaying

    fun initializeSongList(
        navigationOption: String,
        searchQuery: String? = null,
        playlistId: Long? = null
    ) {
        when (navigationOption) {
            NavigationOptions.HOME.navigationName -> {
                _songList.value = songRepository.getAllSongs()
            }

            NavigationOptions.FAVORITE.navigationName -> {
                _songList.value = songRepository.getFavoriteSongs()
            }

            NavigationOptions.RECENT.navigationName -> {
                _songList.value = songRepository.getRecentSongs()
            }

            NavigationOptions.PLAYLIST.navigationName -> {
                _songList.value = songRepository.getPlaylistSongs(playlistId!!)
            }

            NavigationOptions.SEARCH.navigationName -> {
                _songList.value = songRepository.search(searchQuery!!)
            }

            else -> {}
        }
    }

    fun setCurrentSong(song: Song) {
        _currentSong.value = song
    }

    fun setSongList(list: List<Song>) {
        _songList.value = list
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun setIsPlaying(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    fun playNextSong() {
        _index.value = (_index.value?.plus(1))?.rem(_songList.value?.size!!)
        _currentSong.value = _songList.value?.get(_index.value!!)
        _isPlaying.value = true
    }

    fun playPreviousSong() {
        _index.value =
            (_index.value?.minus(1)?.plus(_songList.value?.size!!))?.rem(_songList.value?.size!!)
        _currentSong.value = _songList.value?.get(_index.value!!)
        _isPlaying.value = true
    }

    fun playSong(song: Song) {
        _currentSong.value = song
        _index.value = _songList.value?.indexOf(song)
        _isPlaying.value = true
    }

    fun pauseSong() {
        _isPlaying.value = false
    }

    fun convertTime(time: Long): String {
        val totalSeconds = time / 1000
        val minutes = (totalSeconds / 60).toString()
        val seconds = (totalSeconds % 60).toString()
        return "${minutes.padStart(2, '0')}:${seconds.padStart(2, '0')}"
    }

}