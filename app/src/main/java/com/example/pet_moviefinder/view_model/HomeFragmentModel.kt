package com.example.pet_moviefinder.view_model

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pet_moviefinder.model.IFilmRepositoryController
import com.example.pet_moviefinder.model.INavigationController
import com.example.pet_moviefinder.data.entity.Film
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFragmentModel(
    private val navigation: INavigationController,
    private val repositoryController: IFilmRepositoryController,
    private val handle: SavedStateHandle
) : ViewModel() {

    var filmList: BehaviorSubject<List<Film>> = repositoryController.getFilmList()

    var searchInFocus = BehaviorSubject.create<Boolean>().apply { onNext(false) }

    var isRefreshing = BehaviorSubject.create<Boolean>().apply { onNext(false) }

    var scrollState: Int = handle.get<Int>(SavedStateHandleKeys.HOME_SCROLL_STATE)?:0
    set(value) {
        field = value
        handle.set(SavedStateHandleKeys.HOME_SCROLL_STATE, field)
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
                adapter.list = repositoryController.getFilmList().value.filter { film ->
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
            isRefreshing.onNext(false)
        }
    }
}