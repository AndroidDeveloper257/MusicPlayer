package uz.mobiler.musicplayer.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_songs",
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["song_id"],
            childColumns = ["song_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["song_id"])]
)
data class FavoriteSongEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favorite_song_id")
    val id: Long = 0L,
    @ColumnInfo(name = "song_id")
    val songId: Long = 0,
    @ColumnInfo(name = "added_at")
    val addedAt: Long = System.currentTimeMillis()
)