package uz.mobiler.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import uz.mobiler.musicplayer.database.entity.FavoriteSongEntity
import uz.mobiler.musicplayer.database.entity.PlaylistEntity
import uz.mobiler.musicplayer.database.entity.PlaylistSongCrossRef
import uz.mobiler.musicplayer.database.entity.RecentSongEntity

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favoriteSong: FavoriteSongEntity)

    @Delete
    suspend fun deleteFromFavorites(favoriteSong: FavoriteSongEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToRecent(recentSong: RecentSongEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Transaction
    suspend fun addSongsToPlaylist(playlistId: Long, songIds: List<Long>) {
        val crossRefs = songIds.map { songId ->
            PlaylistSongCrossRef(playlistId, songId)
        }
        addSongs(crossRefs)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongs(crossRefs: List<PlaylistSongCrossRef>)

    @Query("DELETE FROM favorite_songs")
    suspend fun clearFavoriteSongs()

    @Query("DELETE FROM recent_songs")
    suspend fun clearRecentSongs()

    @Query("DELETE FROM playlists")
    suspend fun clearPlaylists()

    @Query("DELETE FROM playlist_songs")
    suspend fun clearPlaylistSongs()

    @Transaction
    suspend fun clearAll() {
        clearFavoriteSongs()
        clearRecentSongs()
        clearPlaylists()
        clearPlaylistSongs()
    }

    @Query("SELECT * FROM playlists")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_songs WHERE playlist_id = :playlistId")
    suspend fun getPlaylistSongs(playlistId: Long): List<PlaylistSongCrossRef>

    @Query("SELECT * FROM favorite_songs")
    suspend fun getFavoriteSongs(): List<FavoriteSongEntity>

    @Query("SELECT * FROM recent_songs")
    suspend fun getRecentSongs(): List<RecentSongEntity>

}