package uz.mobiler.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import uz.mobiler.musicplayer.database.entity.PlaylistEntity
import uz.mobiler.musicplayer.database.entity.PlaylistSongCrossRef
import uz.mobiler.musicplayer.database.entity.SongEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Delete
    fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongToPlaylist(playlistSongCrossRef: PlaylistSongCrossRef)

    @Delete
    fun removeSongFromPlaylist(playlistSongCrossRef: PlaylistSongCrossRef)

    @Transaction
    @Query("SELECT * FROM songs INNER JOIN playlist_songs ON songs.song_id = playlist_songs.song_id WHERE playlist_songs.playlist_id = :playlistId")
    fun getSongsByPlaylist(playlistId: Long): List<SongEntity>

    @Query("DELETE FROM playlists")
    fun clearPlaylists()

}