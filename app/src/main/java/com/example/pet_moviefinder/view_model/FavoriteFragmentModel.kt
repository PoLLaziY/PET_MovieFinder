package com.example.pet_moviefinder.view_model

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.model.INavigationController

class FavoriteFragmentModel(
    val favoriteController: IFavoriteRepositoryController,
    val navigation: INavigationController
): ViewModel() {

    var listData = favoriteController.getLiveData()

    var searchInFocus = MutableLiveData(false)

    var isRefreshing = MutableLiveData(false)

    fun onQueryTextListener(adapter: FilmViewAdapter) = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            if (!newText.isNullOrBlank()) {
                adapter.list = favoriteController.getLiveData().value?.filter { film ->
                    film.title?.contains(newText, true)?: false
                }
            }
            return true
        }
    }

    val onNavigationClickListener: (itemId: Int) -> Boolean = {
        navigation.onNavigationClick(it)
        true
    }

    fun onFilmItemClick(film: Film) {
        navigation.onFilmItemClick(film)
    }

    fun refreshData() {
        favoriteController.refreshData {
            isRefreshing.postValue(false)
        }
    }
}