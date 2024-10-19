package uz.mobiler.musicplayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.mobiler.musicplayer.database.dao.FavoriteSongDao
import uz.mobiler.musicplayer.database.dao.PlaylistDao
import uz.mobiler.musicplayer.database.dao.RecentSongDao
import uz.mobiler.musicplayer.database.dao.SongDao
import uz.mobiler.musicplayer.database.entity.FavoriteSongEntity
import uz.mobiler.musicplayer.database.entity.PlaylistEntity
import uz.mobiler.musicplayer.database.entity.PlaylistSongCrossRef
import uz.mobiler.musicplayer.database.entity.RecentSongEntity
import uz.mobiler.musicplayer.database.entity.SongEntity

@Database(
    entities = [
        FavoriteSongEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class,
        RecentSongEntity::class,
        SongEntity::class
    ], version = 1
)
abstract class MusicPlayerDatabase : RoomDatabase() {

    abstract fun favoriteSongDao(): FavoriteSongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun recentSongDao(): RecentSongDao
    abstract fun songDao(): SongDao

}