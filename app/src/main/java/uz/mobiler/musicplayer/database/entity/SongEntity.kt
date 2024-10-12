package uz.mobiler.musicplayer.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "song_id")
    val id: Long = 0L,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "artist")
    val artist: String? = null,
    @ColumnInfo(name = "album")
    val album: String? = null,
    @ColumnInfo(name = "duration")
    val duration: Long = 0L,
    @ColumnInfo(name = "file_path")
    val filePath: String,
    @ColumnInfo(name = "album_art_uri")
    val albumArtUri: String? = null,
)