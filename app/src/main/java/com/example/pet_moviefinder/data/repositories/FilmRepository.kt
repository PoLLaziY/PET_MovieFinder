package com.example.pet_moviefinder.data.repositories

import com.example.pet_moviefinder.data.entity.Film

//Репозиторий - хранилище DTO объектов
//Создаётся пустым
//Должен умень получать объекты для сохранения и отдавать
class FilmRepository() {
    private var _list: MutableList<Film> = mutableListOf()

    fun get(index: Int): Film {
        return _list[index]
    }

    fun set(index: Int, element: Film) {
        _list.add(index, element)
    }

    fun add(element: Film) {
        _list.add(element)
    }

    fun size(): Int {
        return _list.size
    }

    fun set(list: List<Film>) {
        _list = list.toMutableList()
    }

    fun add(list: List<Film>?) {
        if (list != null) _list.addAll(list)
    }

    fun getList(): List<Film> {
        return _list
    }

    fun remove(index: Int) {
        _list.removeAt(index)
    }

    fun clear() {
        _list.clear()
    }
}