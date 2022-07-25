package com.example.pet_moviefinder.model

import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.data.repositories.FavoriteFilmRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

interface IFavoriteRepositoryController {
    fun refreshData(doOnLoad: (()->Unit)? = null)
    fun isFavorite(film: Film?): Boolean
    fun addFavorite(film: Film)
    fun removeFavorite(film: Film)
    fun getFilmList(): BehaviorSubject<List<Film>>
}

class FavoriteRepositoryController(
    val favoriteFilmRepository: FavoriteFilmRepository
) : IFavoriteRepositoryController {

    override fun refreshData(doOnLoad: (()->Unit)?) {
        favoriteFilmRepository.refresh()
    }

    override fun isFavorite(film: Film?): Boolean {
        if (film == null) return false
        return favoriteFilmRepository.getFilmList().value?.any {
            it.id == film.id
        } ?: false
    }

    override fun addFavorite(film: Film) {
            favoriteFilmRepository.insert(film)
    }

    override fun removeFavorite(film: Film) {
        favoriteFilmRepository.remote(film)
    }

    override fun getFilmList(): BehaviorSubject<List<Film>> {
        return favoriteFilmRepository.getFilmList()
    }
}