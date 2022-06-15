package com.example.pet_moviefinder.model

import android.widget.Toast
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.data.dao.FavoriteFilmDao
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.data.repositories.FavoriteFilmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface IFavoriteRepositoryController {
    fun refreshData(doOnLoad: (()->Unit)? = null)
    fun isFavorite(film: Film): Boolean
    fun getList(): List<Film>
    fun addFavorite(film: Film)
    fun removeFavorite(film: Film)
}

class FavoriteRepositoryController(
    val favoriteFilmRepository: FavoriteFilmRepository,
    val favoriteFilmDao: FavoriteFilmDao
) : IFavoriteRepositoryController {

    override fun refreshData(doOnLoad: (()->Unit)?) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteFilmRepository.setFavoriteList(favoriteFilmDao.getFavoriteList())
            CoroutineScope(Dispatchers.Main).launch {
                doOnLoad?.invoke()
                if (favoriteFilmRepository.size() == 0) Toast.makeText(App.app, "Favorite Repository is empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun isFavorite(film: Film): Boolean {
        return favoriteFilmRepository.isFavorite(film)
    }

    override fun getList(): List<Film> {
        return favoriteFilmRepository.getFavoriteList()
    }

    override fun addFavorite(film: Film) {
        if (favoriteFilmRepository.addFavorite(film)) {
            CoroutineScope(Dispatchers.IO).launch {
                favoriteFilmDao.insert(film)
            }
        }
    }

    override fun removeFavorite(film: Film) {
        if (favoriteFilmRepository.removeFavorite(film)) {
            CoroutineScope(Dispatchers.IO).launch {
                favoriteFilmDao.delete(film)
            }
        }
    }
}