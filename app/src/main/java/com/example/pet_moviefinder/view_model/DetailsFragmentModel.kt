package com.example.pet_moviefinder.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.model.INavigationController
import com.example.pet_moviefinder.model.NavigationController

class DetailsFragmentModel(
    val favoriteController: IFavoriteRepositoryController,
    val navigationController: INavigationController,
    val handle: SavedStateHandle
) : ViewModel() {

    val film: MutableLiveData<Film?> = MutableLiveData<Film?>()

    private var _isFavoriteInRepository: Boolean = false

    val isFavorite: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        film.observeForever {
            _isFavoriteInRepository = favoriteController.isFavorite(it)
            isFavorite.postValue(_isFavoriteInRepository)
            if (it != null) handle.set(SavedStateHandleKeys.DETAILS_FILM_KEY, it)
        }

        film.postValue(handle.get(SavedStateHandleKeys.DETAILS_FILM_KEY))

        isFavorite.observeForever {
            if (it && !_isFavoriteInRepository) addToFavorite(film.value)
            if (!it && _isFavoriteInRepository) removeFromFavorite(film.value)
        }
    }

    fun addToFavorite(film: Film?) {
        if (film != null) {
            favoriteController.addFavorite(film)
            _isFavoriteInRepository = true
        }
    }

    fun removeFromFavorite(film: Film?) {
        if (film != null) {
            favoriteController.removeFavorite(film)
            _isFavoriteInRepository = false
        }
    }

    fun onShareButtonClick() {
        if (film.value != null) navigationController.onShareButtonClick(film.value!!)
    }
}