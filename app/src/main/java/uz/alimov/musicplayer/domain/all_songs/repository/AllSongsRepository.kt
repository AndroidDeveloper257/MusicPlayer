package uz.alimov.musicplayer.domain.all_songs.repository

import kotlinx.coroutines.flow.Flow
import uz.alimov.musicplayer.domain.Resource
import uz.alimov.musicplayer.domain.all_songs.model.Song
import uz.alimov.musicplayer.domain.all_songs.util.AllSongsError

interface AllSongsRepository {

    fun getAllSongs(): Flow<Resource<List<Song>, AllSongsError>>

}