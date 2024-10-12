package uz.mobiler.musicplayer.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_songs")
data class RecentSongEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recent_song_id")
    val id: Long = 0L,
    @ColumnInfo(name = "song_id")
    val songId: Long = 0,
    @ColumnInfo(name = "played_at")
    val playedAt: Long = System.currentTimeMillis()
)