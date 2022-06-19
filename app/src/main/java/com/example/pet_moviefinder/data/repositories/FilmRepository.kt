package com.example.pet_moviefinder.data.repositories

import androidx.lifecycle.LiveData
import com.example.pet_moviefinder.data.entity.Film

//Репозиторий - хранилище DTO объектов
//Создаётся пустым
//Должен умень получать объекты для сохранения и отдавать
class FilmRepository(liveData: LiveData<List<Film>>) {
    private val liveList: LiveData<List<Film>> = liveData

    fun getLiveData(): LiveData<List<Film>> {
        return liveList
    }

    fun size(): Int {
        return liveList.value?.size?: 0
    }
}