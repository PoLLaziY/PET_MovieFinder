package com.example.pet_moviefinder.view_model

import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.*
import com.example.moviefinder.data.remote_api.TheMovieDbConst
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.model.INavigationController
import kotlinx.coroutines.launch
import java.net.URL

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

    fun onDownloadButtonClick() {
        if (film.value == null) return

        if (navigationController.checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            viewModelScope.launch {
                navigationController.loadToGalleryImage(film.value!!)
                navigationController.makeSnackBar()
            }
        } else {
            navigationController.requestPermissionAndDo(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}