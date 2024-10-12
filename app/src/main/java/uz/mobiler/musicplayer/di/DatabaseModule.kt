package uz.mobiler.musicplayer.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.mobiler.musicplayer.database.MusicPlayerDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MusicPlayerDatabase {
        return Room.databaseBuilder(
            context,
            MusicPlayerDatabase::class.java,
            "music_player_database"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideMusicDao(
        database: MusicPlayerDatabase
    ) = database.musicDao()

    @Provides
    @Singleton
    fun provideContext(
        @ApplicationContext context: Context
    ) = context

}