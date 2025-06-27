package uz.alimov.musicplayer.domain.all_songs.usecase

import uz.alimov.musicplayer.domain.all_songs.repository.AllSongsRepository

class GetAllSongsUseCase(
    private val repository: AllSongsRepository
) {

    fun invoke() = repository.getAllSongs()

}