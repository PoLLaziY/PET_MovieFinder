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

    var searchInFocus = MutableLiveData(false).apply {
        this.observeForever {
            if (it == false && adapter != null) {
                adapter.list = repositoryController.getList()
            }
        }
    }
    var isRefreshing = MutableLiveData(false).apply {
        this.observeForever {
            if (it) {
                repositoryController.refreshData {
                    if (!(searchInFocus.value!!)) adapter.list = it.getList()
                    CoroutineScope(Dispatchers.Main).launch {
                        value = false
                    }
                }
            }
        }
    }

    var onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (!newText.isNullOrBlank()) {
                adapter.list = repositoryController.getList().filter { film ->
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

    val filmItemClickListener = { film: Film -> navigation.onFilmItemClick(film) }

    var adapter = FilmViewAdapter(filmItemClickListener).apply {
        this.list = repositoryController.getList()
        this.doOnListFinished = {
            repositoryController.updateData {
                this.list = it.getList()
            }
        }
    }

    fun refreshFilmList() {
        adapter.list = repositoryController.getList()
    }
}