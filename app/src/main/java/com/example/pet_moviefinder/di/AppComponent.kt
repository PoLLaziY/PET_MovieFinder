package com.example.pet_moviefinder.di

import android.content.Context
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.pet_moviefinder.model.IFilmRepositoryController
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.model.INavigationController
import com.example.pet_moviefinder.di.modules.DataModule
import com.example.pet_moviefinder.di.modules.ControllerModule
import com.example.pet_moviefinder.di.modules.RemoteModule
import com.example.pet_moviefinder.di.modules.ViewModelModule
import com.example.pet_moviefinder.view_model.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, RemoteModule::class, DataModule::class, ControllerModule::class])
abstract class AppComponent() {

    abstract fun getSettingsFragmentModel(): SettingsFragmentModel
    abstract fun getNavigationInterface(): INavigationController
    abstract fun getFilmRepositoryInterface(): IFilmRepositoryController
    abstract fun getFavoriteRepositoryInterface(): IFavoriteRepositoryController


    @Component.Factory
    abstract class Factory {
        abstract fun create(@BindsInstance context: Context): AppComponent
    }

    fun provideDetailsModelFactory(owner: SavedStateRegistryOwner): AbstractSavedStateViewModelFactory {
        return object : AbstractSavedStateViewModelFactory(owner, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return DetailsFragmentModel(
                    navigationController = getNavigationInterface(),
                    favoriteController = getFavoriteRepositoryInterface(),
                    handle = handle
                ) as T
            }
        }
    }

    fun provideHomeModelFactory(owner: SavedStateRegistryOwner): AbstractSavedStateViewModelFactory {
        return object : AbstractSavedStateViewModelFactory(owner, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return HomeFragmentModel(
                    navigation = getNavigationInterface(),
                    repositoryController = getFilmRepositoryInterface(),
                    handle = handle
                ) as T
            }
        }
    }

    fun provideFavoriteModelFactory(owner: SavedStateRegistryOwner): AbstractSavedStateViewModelFactory {
        return object : AbstractSavedStateViewModelFactory(owner, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return FavoriteFragmentModel(
                    navigation = getNavigationInterface(),
                    favoriteController = getFavoriteRepositoryInterface(),
                    handle = handle
                ) as T
            }
        }
    }
}
