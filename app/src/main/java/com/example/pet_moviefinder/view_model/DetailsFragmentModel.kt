package com.example.pet_moviefinder.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.model.INavigationController

class DetailsFragmentModel(
    val favoriteController: IFavoriteRepositoryController,
    val navigationController: INavigationController,
    val handle: SavedStateHandle
) : ViewModel() {

    val film: MutableLiveData<Film?> = MutableLiveData<Film?>()
    private val filmObserver = Observer<Film?> { it ->
            _isFavoriteInRepository = favoriteController.isFavorite(it)
            isFavorite.postValue(_isFavoriteInRepository)
            if (it != null) handle.set(SavedStateHandleKeys.DETAILS_FILM_KEY, it)
        }

    private var _isFavoriteInRepository: Boolean = false

    val isFavorite: MutableLiveData<Boolean> = MutableLiveData(false)
    private val isFavoriteObserver = Observer<Boolean> { it ->
        if (it == true && !_isFavoriteInRepository) addToFavorite(film.value)
        if (it != true && _isFavoriteInRepository) removeFromFavorite(film.value)
    }

    init {
        film.observeForever (filmObserver)

        film.postValue(handle.get(SavedStateHandleKeys.DETAILS_FILM_KEY))

        isFavorite.observeForever (isFavoriteObserver)
    }

    override fun onCleared() {
        film.removeObserver(filmObserver)
        isFavorite.removeObserver(isFavoriteObserver)
        super.onCleared()
    }

    private fun addToFavorite(film: Film?) {
        if (film != null) {
            favoriteController.addFavorite(film)
            _isFavoriteInRepository = true
        }
    }

    private fun removeFromFavorite(film: Film?) {
        if (film != null) {
            favoriteController.removeFavorite(film)
            _isFavoriteInRepository = false
        }
    }

    fun onShareButtonClick() {
        if (film.value != null) navigationController.onShareButtonClick(film.value!!)
    }
}