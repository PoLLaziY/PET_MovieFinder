package com.example.pet_moviefinder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pet_moviefinder.data.dao.FavoriteFilmDao
import com.example.pet_moviefinder.data.dao.FilmDao
import com.example.pet_moviefinder.data.entity.FavoriteFilm
import com.example.pet_moviefinder.data.entity.Film

@Database(entities = [Film::class, FavoriteFilm::class], version = FilmDb.DB_VERSION, exportSchema = true)
abstract class FilmDb: RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun favoriteFilmDao(): FavoriteFilmDao

    companion object {
        const val DB_NAME = "films_db"
        const val DB_VERSION = 5
    }
}