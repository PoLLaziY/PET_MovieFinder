package com.example.pet_moviefinder.data.dao

import androidx.room.*
import com.example.pet_moviefinder.data.entity.FavoriteFilm
import com.example.pet_moviefinder.data.entity.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

@Dao
interface FavoriteFilmDao {
    @Query("SELECT * FROM ${FavoriteFilm.Fields.TABLE_NAME}")
    fun getFavoriteFilmList(): Flow<List<FavoriteFilm>>

    fun getFavoriteList(): Flow<List<Film>> {
        return getFavoriteFilmList().transform {value ->
            emit(value)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteFilm: FavoriteFilm): Long

    @Query(
        "Replace INTO ${FavoriteFilm.Fields.TABLE_NAME} (${Film.Fields.TITLE}," +
                "${Film.Fields.DESCRIPTION}, ${Film.Fields.ICON_URL}, ${Film.Fields.ID}," +
                "${Film.Fields.RATING}) \n" +
                "VALUES (:title, :description, :iconURL, :id, :ratting)"
    )
    fun insertFields(
        title: String?,
        description: String?,
        iconURL: String?,
        id: Int,
        ratting: Double?
    ): Long

    fun insert(film: Film) {
        insertFields(film.title, film.description, film.iconUrl, film.id, film.rating)
    }

    @Delete
    fun delete(favoriteFilm: FavoriteFilm)

    fun delete(film: Film) {
        deleteForId(film.id)
    }

    @Query("Delete From ${FavoriteFilm.Fields.TABLE_NAME} where id == :index")
    fun deleteForId(index: Int)
}