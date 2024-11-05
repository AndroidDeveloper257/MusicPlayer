package uz.mobiler.musicplayer.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mobiler.musicplayer.models.Song
import uz.mobiler.musicplayer.repository.SongRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _searchResult = MutableLiveData<List<Song>>()
    val searchResult get() = _searchResult

    init {
        search("")
    }

    fun search(searchQuery: String) {
        _searchResult.value = songRepository.search(searchQuery)
    }

}