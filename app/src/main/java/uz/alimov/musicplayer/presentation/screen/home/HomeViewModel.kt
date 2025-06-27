package uz.alimov.musicplayer.presentation.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.alimov.musicplayer.domain.Resource
import uz.alimov.musicplayer.domain.all_songs.usecase.GetAllSongsUseCase
import uz.alimov.musicplayer.domain.all_songs.util.AllSongsError

class HomeViewModel(
    private val getAllSongsUseCase: GetAllSongsUseCase
) : ViewModel() {

    private val TAG = "HomeViewModel"

    private val _state = MutableStateFlow(HomeState())
    val state get() = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect get() = _effect.asSharedFlow()

    init {
        fetchSongs()
    }

    fun onIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                HomeIntent.OnOpenSettingsConfirmed -> {
                    _state.update {
                        it.copy(
                            showOpenSettingsDialog = false
                        )
                    }
                    sendEffect(HomeEffect.OpenSettings)
                }

                is HomeIntent.OnPermissionRequestResult -> {
                    if (intent.isGranted) {
                        fetchSongs()
                    } else {
                        sendEffect(HomeEffect.PermissionDenied)
                    }
                }

                HomeIntent.OnRationaleConfirmed -> {
                    sendEffect(HomeEffect.RequestPermission)
                }

                HomeIntent.OnShowOpenSettings -> {
                    _state.update {
                        it.copy(
                            showOpenSettingsDialog = true
                        )
                    }
                }

                HomeIntent.OnShowRationale -> {
                    _state.update {
                        it.copy(
                            showRationale = true
                        )
                    }
                }

                HomeIntent.HideError -> {
                    _state.update {
                        it.copy(
                            error = null
                        )
                    }
                }
            }
        }
    }

    fun fetchSongs() {
        viewModelScope.launch {
            Log.d(TAG, "fetchSongs: fetching songs")
            getAllSongsUseCase.invoke()
                .onStart {
                    Log.d(TAG, "fetchSongs: onStart block")
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                .catch { throwable ->
                    Log.d(TAG, "fetchSongs: catch block ${throwable.message}")
                    _state.update {
                        it.copy(
                            error = throwable.message ?: "Unknown error",
                            isLoading = false
                        )
                    }
                }
                .collect { resource ->
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    when (resource) {
                        is Resource.Error -> {
                            when (resource.rawResponse) {
                                AllSongsError.PermissionDenied -> {
                                    Log.d(TAG, "fetchSongs: permission denied")
                                    sendEffect(HomeEffect.RequestPermission)
                                }

                                is AllSongsError.UnknownError -> {
                                    Log.d(TAG, "fetchSongs: unknown error ${resource.rawResponse.message}")
                                    _state.update {
                                        it.copy(
                                            error = resource.rawResponse.toString()
                                        )
                                    }
                                }
                            }
                        }

                        is Resource.Success -> {
                            Log.d(TAG, "fetchSongs: success ${resource.data} songs fetched")
                            _state.update {
                                it.copy(
                                    songList = resource.data
                                )
                            }
                        }
                    }
                }
        }
    }

    private suspend fun sendEffect(effect: HomeEffect) {
        _effect.emit(effect)
    }

}