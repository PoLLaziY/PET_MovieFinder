package com.example.pet_moviefinder.di.modules

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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