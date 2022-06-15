package com.example.pet_moviefinder.data.remote_api

import com.example.pet_moviefinder.data.dto.TmdbResultsDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//интерфейс работы с базой данных
interface TheMovieDbAPI {

    //метод получения списка фильмов
    @GET("movie/{category}")
    fun getFilmList(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbResultsDto>
}