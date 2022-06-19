package com.example.pet_moviefinder.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pet_moviefinder.data.entity.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM ${Film.Fields.TABLE_NAME}")
    fun getFilmList(): LiveData<List<Film>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Film): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(films: List<Film>)

    @Query("DELETE FROM ${Film.Fields.TABLE_NAME}")
    fun clear()
}