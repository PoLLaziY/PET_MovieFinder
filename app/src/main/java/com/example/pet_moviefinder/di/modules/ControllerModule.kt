package com.example.pet_moviefinder.di.modules

import com.example.pet_moviefinder.data.PreferencesProvider
import com.example.pet_moviefinder.data.remote_api.TheMovieDbAPI
import com.example.pet_moviefinder.data.repositories.FavoriteFilmRepository
import com.example.pet_moviefinder.data.repositories.FilmRepository
import com.example.pet_moviefinder.model.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ControllerModule {

    @Provides
    @Singleton
    fun provideNavigationController(): INavigationController {
        return NavigationController()
    }

    @Provides
    @Singleton
    fun provideFavoriteRepositoryController(
        favoriteFilmRepository: FavoriteFilmRepository
    ): IFavoriteRepositoryController {
        return FavoriteRepositoryController(
            favoriteFilmRepository = favoriteFilmRepository
        )
    }

    @Provides
    @Singleton
    fun provideFilmRepositoryController(
        service: TheMovieDbAPI,
        repository: FilmRepository,
        preferencesProvider: PreferencesProvider
    ): IFilmRepositoryController {
        return FilmRepositoryController(
            service = service,
            filmRepository = repository,
            preferencesProvider = preferencesProvider
        )
    }

}