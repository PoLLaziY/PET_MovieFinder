package com.example.pet_moviefinder.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.model.INavigationController
import com.example.pet_moviefinder.model.NavigationController

class DetailsFragmentModel(
    val favoriteController: IFavoriteRepositoryController,
    val navigationController: INavigationController
) : ViewModel() {

    var film: Film? = null
        set(value) {
            field = value
            if (value != null) {
                isFavoriteFromRepository = favoriteController.isFavorite(value)
                isFavorite.value = isFavoriteFromRepository
            }
        }

    var isFavoriteFromRepository: Boolean = false

    val isFavorite = MutableLiveData(false).apply {
        this.observeForever {
            if (it && !isFavoriteFromRepository) addToFavorite(film!!)
            if (!it && isFavoriteFromRepository) removeFromFavorite(film!!)
        }
    }

    fun addToFavorite(film: Film) {
        favoriteController.addFavorite(film)
        isFavoriteFromRepository = true
    }

    fun removeFromFavorite(film: Film) {
        favoriteController.removeFavorite(film)
        isFavoriteFromRepository = false
    }

    fun onShareButtonClick() {
        if (film != null) navigationController.onShareButtonClick(film!!)
    }
}