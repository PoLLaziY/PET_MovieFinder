package com.example.pet_moviefinder.view_model

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pet_moviefinder.model.IFilmRepositoryController
import com.example.pet_moviefinder.model.INavigationController
import com.example.pet_moviefinder.data.entity.Film
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentModel(
    private val navigation: INavigationController,
    private val repositoryController: IFilmRepositoryController
) : ViewModel() {

    var listData = repositoryController.getLiveData()

    var searchInFocus = MutableLiveData(false)

    var isRefreshing = MutableLiveData(false)

    fun onQueryTextListener(adapter: FilmViewAdapter) = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            if (!newText.isNullOrBlank()) {
                adapter.list = repositoryController.getLiveData().value?.filter { film ->
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
        repositoryController.refreshData {
            isRefreshing.postValue(false)
        }
    }
}