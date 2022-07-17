package com.example.pet_moviefinder.di.modules

import android.content.Context
import androidx.room.Room
import com.example.pet_moviefinder.data.FilmDb
import com.example.pet_moviefinder.data.PreferencesProvider
import com.example.pet_moviefinder.data.repositories.FavoriteFilmRepository
import com.example.pet_moviefinder.data.repositories.FilmRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideFilmRepository(filmDb: FilmDb) = FilmRepository(filmDb.filmDao().getFilmList())

    @Provides
    @Singleton
    fun provideFavoriteFilmRepository(filmDb: FilmDb) = FavoriteFilmRepository(filmDb.favoriteFilmDao().getFavoriteList())

    @Provides
    @Singleton
    fun providePreferencesProvider(context: Context) = PreferencesProvider(context)

    @Provides
    @Singleton
    fun provideFilmDb(context: Context): FilmDb {
        return Room.databaseBuilder(context, FilmDb::class.java, FilmDb.DB_NAME).fallbackToDestructiveMigrationOnDowngrade().fallbackToDestructiveMigration().build()
    }
}