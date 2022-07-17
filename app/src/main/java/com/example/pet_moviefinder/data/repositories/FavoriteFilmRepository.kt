package com.example.pet_moviefinder.data.repositories

import com.example.pet_moviefinder.data.entity.Film
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteFilmRepository (list: Flow<List<Film>>) {
    private val _list = MutableStateFlow<List<Film>>(emptyList())
    init {
        CoroutineScope(Dispatchers.IO).launch {
            list.collect {
                _list.emit(it)
            }
        }
    }
    fun size(): Int {
        return _list.value.size
    }

    fun getFilmList(): StateFlow<List<Film>> {
        return _list
    }

}