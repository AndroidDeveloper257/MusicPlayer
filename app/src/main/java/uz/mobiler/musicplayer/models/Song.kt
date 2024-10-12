package uz.mobiler.musicplayer.models

data class Song(
    val title: String = "",
    val artist: String? = null,
    val album: String? = null,
    val duration: Long = 0L,
    val filePath: String,
    val albumArtUri: String? = null
)