package com.example.pet_moviefinder.data.entity

import androidx.room.TypeConverter

class FilmConverter {
    @TypeConverter
    fun favoriteFilmToFilm(favoriteFilm: FavoriteFilm): Film {
        return Film(
            id = favoriteFilm.id,
            rating = favoriteFilm.rating,
            iconUrl = favoriteFilm.iconUrl,
            description = favoriteFilm.description,
            title = favoriteFilm.title
        )
    }
    @TypeConverter
    fun filmToFavoriteFilm(film: Film): FavoriteFilm {
        return FavoriteFilm(id = film.id,
            rating = film.rating,
            iconUrl = film.iconUrl,
            description = film.description,
            title = film.title)
    }
}