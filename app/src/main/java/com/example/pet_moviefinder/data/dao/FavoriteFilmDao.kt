package com.example.pet_moviefinder.data.dao

import androidx.room.*
import com.example.pet_moviefinder.data.entity.FavoriteFilm
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.data.entity.FilmConverter

@Dao
@TypeConverters(FilmConverter::class)
interface FavoriteFilmDao {
    @Query("SELECT * FROM ${FavoriteFilm.Fields.TABLE_NAME}")
    fun getFavoriteFilmList(): List<FavoriteFilm>

    fun getFavoriteList(): List<Film> {
        return getFavoriteFilmList().map { FilmConverter().favoriteFilmToFilm(it) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteFilm: FavoriteFilm): Long

    fun insert(film: Film): Long {
        return insert(FilmConverter().filmToFavoriteFilm(film))
    }

    @Delete
    fun delete(favoriteFilm: FavoriteFilm)

    fun delete(film: Film) {
        delete(FilmConverter().filmToFavoriteFilm(film))
    }
}