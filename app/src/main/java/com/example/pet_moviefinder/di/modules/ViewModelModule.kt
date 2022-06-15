package com.example.pet_moviefinder.di.modules

import com.example.pet_moviefinder.model.IFilmRepositoryController
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.model.INavigationController
import com.example.pet_moviefinder.data.PreferencesProvider
import com.example.pet_moviefinder.view_model.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun provideHomeFragmentModel(
        navigation: INavigationController,
        repositoryController: IFilmRepositoryController
    ): HomeFragmentModel {
        return HomeFragmentModel(
            navigation = navigation,
            repositoryController = repositoryController
        )
    }

    @Provides
    @Singleton
    fun provideDetailsFragmentModel(favoriteController: IFavoriteRepositoryController, navigation: INavigationController): DetailsFragmentModel {
        return DetailsFragmentModel(favoriteController = favoriteController, navigationController = navigation)
    }

    @Provides
    @Singleton
    fun provideFavoriteFragmentModel(
        favoriteController: IFavoriteRepositoryController,
        navigation: INavigationController
    ): FavoriteFragmentModel {
        return FavoriteFragmentModel(
            favoriteController = favoriteController,
            navigation = navigation
        )
    }

    @Provides
    @Singleton
    fun provideSettingsFragmentModel(
        preferencesProvider: PreferencesProvider,
        repositoryController: IFilmRepositoryController,
        navigation: INavigationController
    ): SettingsFragmentModel {
        return SettingsFragmentModel(
            preferencesProvider = preferencesProvider,
            repositoryController = repositoryController,
            navigation = navigation
        )
    }
}