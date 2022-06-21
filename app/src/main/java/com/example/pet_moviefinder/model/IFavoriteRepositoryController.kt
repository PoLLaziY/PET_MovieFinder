package com.example.pet_moviefinder.model

import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.data.dao.FavoriteFilmDao
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.data.repositories.FavoriteFilmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface IFavoriteRepositoryController {
    fun refreshData(doOnLoad: (()->Unit)? = null)
    fun isFavorite(film: Film?): Boolean
    fun addFavorite(film: Film)
    fun removeFavorite(film: Film)
    fun getLiveData(): LiveData<List<Film>>
}

class FavoriteRepositoryController(
    val favoriteFilmRepository: FavoriteFilmRepository,
    val favoriteFilmDao: FavoriteFilmDao
) : IFavoriteRepositoryController {

    override fun refreshData(doOnLoad: (()->Unit)?) {
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.Main).launch {
                doOnLoad?.invoke()
                if (favoriteFilmRepository.size() == 0) Toast.makeText(App.app, "Favorite Repository is empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun isFavorite(film: Film?): Boolean {
        if (film == null) return false
        return favoriteFilmRepository.getLiveData().value?.any {
            it.id == film.id
        } ?: false
    }

    override fun addFavorite(film: Film) {
            CoroutineScope(Dispatchers.IO).launch {
                favoriteFilmDao.insert(film)
            }
    }

    override fun removeFavorite(film: Film) {
            CoroutineScope(Dispatchers.IO).launch {
                favoriteFilmDao.delete(film)
        }
    }

    override fun getLiveData(): LiveData<List<Film>> {
        return favoriteFilmRepository.getLiveData()
    }
}