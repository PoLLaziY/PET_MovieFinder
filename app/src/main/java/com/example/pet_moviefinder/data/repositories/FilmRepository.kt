package com.example.pet_moviefinder.data.repositories

import com.example.pet_moviefinder.data.dao.FilmDao
import com.example.pet_moviefinder.data.entity.Film
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Репозиторий - хранилище DTO объектов
//Создаётся пустым
//Должен умень получать объекты для сохранения и отдавать
class FilmRepository(
    val filmDao: FilmDao
) {
    private val _list = BehaviorSubject.create<List<Film>>()
    private var subscribe: Disposable? = null

    init {
        refresh()
    }

    fun size(): Int {
        return _list.value?.size ?: 0
    }

    fun getFilmList(): BehaviorSubject<List<Film>> {
        return _list
    }

    fun insert(film: Film) {
        Observable.create<Film> {
            it.onNext(film)
            it.onComplete()
        }.observeOn(Schedulers.io())
            .subscribe {
                filmDao.insert(it)
            }
    }

    fun refresh() {
        subscribe?.dispose()
        subscribe = filmDao.getFilmList().subscribe {
            _list.onNext(it)
        }
    }
}