package uz.alimov.musicplayer.domain.all_songs.model

import android.net.Uri

data class Song(
    val id: String,
    val uri: Uri,
    val title: String,
    val artist: String,
    val album: String,
    val albumArtUri: Uri,
    val duration: Long
)