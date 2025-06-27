package uz.alimov.musicplayer.data.all_songs

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uz.alimov.musicplayer.data.all_songs.repository.AllSongsRepositoryImpl
import uz.alimov.musicplayer.domain.all_songs.repository.AllSongsRepository
import uz.alimov.musicplayer.domain.all_songs.usecase.GetAllSongsUseCase
import uz.alimov.musicplayer.presentation.screen.home.HomeViewModel

val allSongsModule = module {
    single<AllSongsRepository> {
        AllSongsRepositoryImpl(
            context = androidContext()
        )
    }
    single {
        GetAllSongsUseCase(
            repository = get()
        )
    }
    viewModel {
        HomeViewModel(
            getAllSongsUseCase = get()
        )
    }
}