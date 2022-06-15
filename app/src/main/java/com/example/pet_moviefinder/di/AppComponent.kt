package com.example.pet_moviefinder.di

import android.content.Context
import com.example.pet_moviefinder.model.IFilmRepositoryController
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.model.INavigationController
import com.example.pet_moviefinder.data.entity.Film
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
abstract class AppComponent {

    abstract fun getHomeModel(): HomeFragmentModel
    abstract fun getDetailsFragmentModel(): DetailsFragmentModel
    abstract fun getFavoriteFragmentModel(): FavoriteFragmentModel
    abstract fun getSettingsFragmentModel(): SettingsFragmentModel
    abstract fun getNavigationInterface(): INavigationController
    abstract fun getDataUpdateInterface(): IFilmRepositoryController
    abstract fun getFavoriteRepositoryInterface(): IFavoriteRepositoryController

    fun getDetailsFragmentModel(film: Film): DetailsFragmentModel {
        return getDetailsFragmentModel().apply { this.film = film }
    }


    @Component.Factory
    abstract class Factory {
        abstract fun create(@BindsInstance context: Context): AppComponent
    }
}
