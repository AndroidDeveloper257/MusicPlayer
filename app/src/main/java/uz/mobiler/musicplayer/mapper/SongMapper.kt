package uz.mobiler.musicplayer.mapper

import uz.mobiler.musicplayer.database.entity.SongEntity
import uz.mobiler.musicplayer.models.Song

fun SongEntity.toSong(): Song {
    return Song(
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        filePath = filePath,
    )
}

fun Song.toEntity(): SongEntity {
    return SongEntity(
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        filePath = filePath,
    )
}