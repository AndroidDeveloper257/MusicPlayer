package uz.mobiler.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.mobiler.musicplayer.database.entity.SongEntity

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSong(song: SongEntity): Long

    @Query("SELECT * FROM songs")
    fun getAllSongs(): List<SongEntity>

    @Query("SELECT * FROM songs WHERE song_id = :songId")
    fun getSongById(songId: Long): SongEntity?

    @Query("SELECT * FROM songs WHERE file_path = :filePath")
    fun getSongByFilePath(filePath: String): SongEntity?

    @Query("SELECT COUNT(*) > 0 FROM songs WHERE file_path = :filePath")
    fun isSongExisted(filePath: String): Boolean

}