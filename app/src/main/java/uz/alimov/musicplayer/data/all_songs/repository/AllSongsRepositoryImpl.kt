package uz.alimov.musicplayer.data.all_songs.repository

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.alimov.musicplayer.domain.Resource
import uz.alimov.musicplayer.domain.all_songs.model.Song
import uz.alimov.musicplayer.domain.all_songs.repository.AllSongsRepository
import uz.alimov.musicplayer.domain.all_songs.util.AllSongsError
import uz.alimov.musicplayer.presentation.util.PermissionUtils.mediaPermission

class AllSongsRepositoryImpl(
    private val context: Context
) : AllSongsRepository {

    @SuppressLint("Recycle", "UseKtx")
    override fun getAllSongs(): Flow<Resource<List<Song>, AllSongsError>> = flow {
        if (context.checkSelfPermission(
                mediaPermission
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            emit(Resource.Error(AllSongsError.PermissionDenied))
            return@flow
        }
        try {
            val songs = mutableListOf<Song>()
            val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION
            )

            val cursor = context.contentResolver.query(
                collection,
                projection,
                "${MediaStore.Audio.Media.IS_MUSIC} != 0",
                null,
                "${MediaStore.Audio.Media.DATE_ADDED} DESC"
            ) ?: run {
                emit(Resource.Error(AllSongsError.UnknownError("Something went wrong")))
                return@flow
            }

            cursor.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val albumArtBaseUri = Uri.parse("content://media/external/audio/albumart")
                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(collection, id)
                    val title = it.getString(titleColumn) ?: "Unknown title"
                    val artist = it.getString(artistColumn) ?: "Unknown artist"
                    val album = it.getString(albumColumn) ?: "Unknown album"
                    val duration = it.getLong(durationColumn)
                    val albumId = it.getLong(albumIdColumn)
                    val albumArtUri = ContentUris.withAppendedId(albumArtBaseUri, albumId)

                    songs.add(
                        Song(
                            id = id.toString(),
                            uri = uri,
                            title = title,
                            artist = artist,
                            album = album,
                            albumArtUri = albumArtUri,
                            duration = duration
                        )
                    )
                }
            }
            emit(Resource.Success(songs))
        } catch (_: SecurityException) {
            emit(Resource.Error(AllSongsError.PermissionDenied))
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    AllSongsError.UnknownError(
                        e.message ?: "Something went wrong"
                    )
                )
            )
        }
    }.flowOn(Dispatchers.IO)
}