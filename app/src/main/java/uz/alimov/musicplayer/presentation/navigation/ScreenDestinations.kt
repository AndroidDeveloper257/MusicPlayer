package uz.alimov.musicplayer.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Settings

@Serializable
object Favorites

@Serializable
object Playlists

@Serializable
object Recent

@Serializable
data class Playlist(val playListId: Long)