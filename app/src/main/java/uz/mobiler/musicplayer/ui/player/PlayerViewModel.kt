package uz.mobiler.musicplayer.ui.player

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mobiler.musicplayer.repository.SongRepository
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val application: Application
) : ViewModel() {
    
}