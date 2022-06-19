package com.example.pet_moviefinder.data.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.*
import com.example.pet_moviefinder.data.entity.FavoriteFilm
import com.example.pet_moviefinder.data.entity.Film

@Dao
interface FavoriteFilmDao {
    @Query("SELECT * FROM ${FavoriteFilm.Fields.TABLE_NAME}")
    fun getFavoriteFilmList(): LiveData<List<FavoriteFilm>>

    fun getFavoriteList(): LiveData<List<Film>> {
        return Transformations.map(getFavoriteFilmList()) {
            return@map object : AbstractList<Film>() {
                override val size: Int
                    get() = it.size

                override fun get(index: Int): Film {
                    return it[index]
                }
            }
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