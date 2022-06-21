package com.example.pet_moviefinder.view_model

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.model.IFavoriteRepositoryController
import com.example.pet_moviefinder.model.INavigationController

class FavoriteFragmentModel(
    val favoriteController: IFavoriteRepositoryController,
    val navigation: INavigationController,
    private val handle: SavedStateHandle
): ViewModel() {

    var listData = favoriteController.getLiveData()

    var searchInFocus = MutableLiveData(false)

    var isRefreshing = MutableLiveData(false)

    var scrollState: Int = handle.get<Int>(SavedStateHandleKeys.FAVORITE_SCROLL_STATE)?:0
        set(value) {
            field = value
            handle.set(SavedStateHandleKeys.FAVORITE_SCROLL_STATE, field)
        }

    val rvScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            scrollState = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        }
    }

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