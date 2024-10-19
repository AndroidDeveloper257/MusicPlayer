package uz.mobiler.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import uz.mobiler.musicplayer.database.entity.RecentSongEntity
import uz.mobiler.musicplayer.database.entity.SongEntity

@Dao
interface RecentSongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecentSong(recentSong: RecentSongEntity)

    @Transaction
    @Query("SELECT * FROM songs INNER JOIN recent_songs ON songs.song_id = recent_songs.song_id ORDER BY recent_songs.played_at DESC")
    fun getRecentSongs(): List<SongEntity>

    @Query("DELETE FROM recent_songs")
    fun clearRecentSongs()

    @Query("UPDATE recent_songs SET played_at = :playedAt WHERE song_id = :songId")
    fun updatePlayedAt(songId: Long, playedAt: Long)

}