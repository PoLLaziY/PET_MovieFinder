package com.example.pet_moviefinder.data.repositories

import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.data.entity.Film

class FavoriteFilmRepository (liveData: LiveData<List<Film>>) {
    private val liveList: LiveData<List<Film>> = liveData

    fun getLiveData(): LiveData<List<Film>> {
        return liveList
    }

    fun size(): Int {
        return liveList.value?.size?: 0
    }
}