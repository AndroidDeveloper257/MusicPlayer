package uz.mobiler.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import uz.mobiler.musicplayer.database.entity.FavoriteSongEntity
import uz.mobiler.musicplayer.database.entity.SongEntity

@Dao
interface FavoriteSongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteSong(favoriteSong: FavoriteSongEntity)

    @Delete
    fun removeFavoriteSong(favoriteSong: FavoriteSongEntity)

    @Transaction
    @Query("SELECT * FROM songs INNER JOIN favorite_songs ON songs.song_id = favorite_songs.song_id")
    fun getFavoriteSongs(): List<SongEntity>

    @Query("SELECT COUNT(*) FROM favorite_songs WHERE song_id = :songId")
    fun countFavorite(songId: Long): Int

    @Query("DELETE FROM favorite_songs")
    fun clearFavoriteSongs()

}