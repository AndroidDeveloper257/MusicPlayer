package uz.mobiler.musicplayer.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.repository.SongRepository
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _favoriteSongs = MutableLiveData<List<Song>>()
    val favoriteSongs get() = _favoriteSongs

    init {
        fetchFavoriteSongs()
    }

    private fun fetchFavoriteSongs() {
        val favoriteSongs = songRepository.getFavoriteSongs()
        _favoriteSongs.value = favoriteSongs
    }

    fun addFavoriteSong(song: Song) {
        songRepository.insertFavoriteSong(song)
        fetchFavoriteSongs()
    }

    fun removeFavoriteSong(song: Song) {
        songRepository.removeFavoriteSong(song)
        fetchFavoriteSongs()
    }

}