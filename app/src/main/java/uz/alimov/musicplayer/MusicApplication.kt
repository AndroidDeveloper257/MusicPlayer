package uz.alimov.musicplayer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.alimov.musicplayer.data.all_songs.allSongsModule

class MusicApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MusicApplication)
            modules(
                allSongsModule
            )
        }
    }
}