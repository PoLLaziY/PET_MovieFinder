package com.example.pet_moviefinder.view_model

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.model.INavigationController

class FavoriteFragmentModel(
    val favoriteController: IFavoriteRepositoryController,
    val navigation: INavigationController
): ViewModel() {

    val onNavigationClickListener: (itemId: Int) -> Boolean = {
        navigation.onNavigationClick(it)
        true
    }

    val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (!newText.isNullOrBlank()) {
                adapter.list = favoriteController.getList().filter { film ->
                    film.title?.contains(newText, true)?: false
                }
            }
            return true
        }
    }

    val searchInFocus = MutableLiveData(false)
    val adapter: FilmViewAdapter = FilmViewAdapter {
        navigation.onFilmItemClick(it)
    }

    init {
        refreshFilmList()
    }

    fun refreshFilmList() {
        favoriteController.refreshData() {
            adapter.list = favoriteController.getList().toList()
        }
    }
}