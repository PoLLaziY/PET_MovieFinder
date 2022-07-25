package com.example.pet_moviefinder.di.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pet_moviefinder.data.FilmDb
import com.example.pet_moviefinder.data.PreferencesProvider
import com.example.pet_moviefinder.data.dao.FavoriteFilmDao
import com.example.pet_moviefinder.data.dao.FilmDao
import com.example.pet_moviefinder.data.repositories.FavoriteFilmRepository
import com.example.pet_moviefinder.data.repositories.FilmRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideFilmRepository(filmDb: FilmDb) = FilmRepository(filmDao = filmDb.filmDao())

    @Provides
    @Singleton
    fun provideFavoriteFilmRepository(filmDb: FilmDb) = FavoriteFilmRepository(favoriteFilmDao = filmDb.favoriteFilmDao())

    @Provides
    @Singleton
    fun providePreferencesProvider(context: Context) = PreferencesProvider(context)

    @Provides
    @Singleton
    fun provideFilmDb(context: Context): FilmDb {
        return Room.databaseBuilder(context, FilmDb::class.java, FilmDb.DB_NAME).fallbackToDestructiveMigrationOnDowngrade().fallbackToDestructiveMigration().build()
    }
}