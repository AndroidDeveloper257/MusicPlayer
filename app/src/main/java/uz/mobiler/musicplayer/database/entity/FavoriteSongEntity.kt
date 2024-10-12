package uz.mobiler.musicplayer.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_songs")
data class FavoriteSongEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favorite_song_id")
    val id: Long = 0L,
    @ColumnInfo(name = "song_id")
    val songId: Long = 0,
    @ColumnInfo(name = "added_at")
    val addedAt: Long = System.currentTimeMillis()
)