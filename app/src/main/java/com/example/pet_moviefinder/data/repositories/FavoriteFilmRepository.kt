package com.example.pet_moviefinder.data.repositories

import com.example.pet_moviefinder.data.entity.Film

class FavoriteFilmRepository {
    private var _list = mutableListOf<Film>()

    fun isFavorite(film: Film): Boolean {
        _list.forEach {
            if (film.id == it.id) return true
        }
        return false
    }

    fun getFavoriteList(): List<Film> {
        return _list
    }

    fun addFavorite(film: Film): Boolean {
        return _list.add(film)
    }
    fun removeFavorite(film: Film): Boolean {
        return _list.remove(film)
    }
    fun setFavoriteList(list: List<Film>) {
        _list = list.toMutableList()
    }

    fun size(): Int {
        return _list.size
    }
}