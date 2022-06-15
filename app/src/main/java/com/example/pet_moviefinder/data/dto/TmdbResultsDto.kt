package com.example.pet_moviefinder.data.dto

import com.example.pet_moviefinder.data.entity.Film
import com.google.gson.annotations.SerializedName

data class TmdbResultsDto(
    @SerializedName("page") val page : Int,
    @SerializedName("results") val results : List<Film>,
    @SerializedName("total_results") val total_results : Int,
    @SerializedName("total_pages") val total_pages : Int
)
