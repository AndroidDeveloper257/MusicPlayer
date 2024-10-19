package uz.mobiler.musicplayer.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import uz.mobiler.musicplayer.database.dao.FavoriteSongDao
import uz.mobiler.musicplayer.database.dao.PlaylistDao
import uz.mobiler.musicplayer.database.dao.RecentSongDao
import uz.mobiler.musicplayer.database.dao.SongDao
import uz.mobiler.musicplayer.database.entity.FavoriteSongEntity
import uz.mobiler.musicplayer.database.entity.PlaylistEntity
import uz.mobiler.musicplayer.database.entity.PlaylistSongCrossRef
import uz.mobiler.musicplayer.database.entity.RecentSongEntity
import uz.mobiler.musicplayer.mapper.toEntity
import uz.mobiler.musicplayer.mapper.toSong
import uz.mobiler.musicplayer.models.Song
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val favoriteSongDao: FavoriteSongDao,
    private val playlistDao: PlaylistDao,
    private val recentSongDao: RecentSongDao,
    private val songDao: SongDao,
    private val context: Context
) {

    /**
     * to get all songs from device
     */
    fun getAllSongs(): List<Song> {
        val songList = mutableListOf<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
        )
        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            while (cursor.moveToNext()) {
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)
                val duration = cursor.getString(durationColumn)
                val filePath = cursor.getString(dataColumn)
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    cursor.getLong(albumIdColumn)
                ).toString()
                songList.add(
                    Song(
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration.toLong(),
                        filePath = filePath,
                        albumArtUri = albumArtUri
                    )
                )
            }
        }
        return songList
    }

    /**
     * to get favorite song, playlists, playlist song and recent song
     */
    fun getFavoriteSongs(): List<Song> {
        return favoriteSongDao.getFavoriteSongs().map {
            it.toSong()
        }
    }

    fun getPlaylists(): List<PlaylistEntity> {
        return playlistDao.getAllPlaylists()
    }

    fun getPlaylistSongs(playlistId: Long): List<Song> {
        return playlistDao.getSongsByPlaylist(playlistId).map {
            it.toSong()
        }
    }

    fun getRecentSongs(): List<Song> {
        return recentSongDao.getRecentSongs().map {
            it.toSong()
        }
    }

    /**
     * to insert favorite song, playlist, playlist song and recent song
     */
    fun insertFavoriteSong(song: Song) {
        favoriteSongDao.insertFavoriteSong(
            FavoriteSongEntity(
                songId = getSongId(song)
            )
        )
    }

    fun insertPlaylist(playlist: PlaylistEntity) {
        playlistDao.insertPlaylist(playlist)
    }

    fun insertSongToPlaylist(playlistId: Long, song: Song) {
        playlistDao.insertSongToPlaylist(
            PlaylistSongCrossRef(
                playlistId = playlistId,
                songId = getSongId(song)
            )
        )
    }

    fun insertRecentSong(song: Song) {
        if (recentSongDao.getRecentSongs().any { it.id == getSongId(song) }) {
            recentSongDao.updatePlayedAt(
                songId = getSongId(song),
                playedAt = System.currentTimeMillis()
            )
        } else {
            recentSongDao.insertRecentSong(
                RecentSongEntity(
                    songId = getSongId(song)
                )
            )
        }
    }

    /**
     * to remove favorite song, playlist, playlist song and recent song
     */
    fun removeFavoriteSong(song: Song) {
        favoriteSongDao.removeFavoriteSong(
            FavoriteSongEntity(
                songId = getSongId(song)
            )
        )
    }

    fun removePlaylist(playlist: PlaylistEntity) {
        playlistDao.deletePlaylist(playlist)
    }

    fun removeSongFromPlaylist(playlistId: Long, song: Song) {
        playlistDao.removeSongFromPlaylist(
            PlaylistSongCrossRef(
                playlistId = playlistId,
                songId = getSongId(song)
            )
        )
    }

    fun removeRecentSong(song: Song) {
        recentSongDao.clearRecentSongs()
    }

    /**
     * to check favorite song, playlist song and recent song if they exist where they're about to be added
     */

    fun isFavorite(song: Song): Boolean {
        return favoriteSongDao.countFavorite(getSongId(song)) > 0
    }

    fun isSongInPlaylist(playlistId: Long, song: Song): Boolean {
        return playlistDao.getSongsByPlaylist(playlistId).any {
            it.id == getSongId(song)
        }
    }

    fun isRecent(song: Song): Boolean {
        return recentSongDao.getRecentSongs().any {
            it.id == getSongId(song)
        }
    }

    /**
     * to clear favorite songs, playlists, playlist songs and recent songs
     */

    fun clearFavoriteSongs() {
        favoriteSongDao.clearFavoriteSongs()
    }

    fun clearPlaylists() {
        playlistDao.clearPlaylists()
    }

    fun clearRecentSongs() {
        recentSongDao.clearRecentSongs()
    }

    private fun getSongId(song: Song): Long {
        return songDao.getSongByFilePath(song.filePath)?.id ?: songDao.insertSong(song.toEntity())
    }

}