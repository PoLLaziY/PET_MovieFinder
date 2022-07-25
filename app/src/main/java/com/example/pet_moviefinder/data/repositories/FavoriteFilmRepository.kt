package com.example.pet_moviefinder.data.repositories

import com.example.pet_moviefinder.data.dao.FavoriteFilmDao
import com.example.pet_moviefinder.data.entity.Film
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FavoriteFilmRepository(
    val favoriteFilmDao: FavoriteFilmDao
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
                favoriteFilmDao.insert(it)
            }
    }

    fun remote(film: Film) {
        Observable.create<Film> {
            it.onNext(film)
            it.onComplete()
        }.observeOn(Schedulers.io())
            .subscribe {
                favoriteFilmDao.delete(it)
            }
    }

    fun refresh() {
        subscribe?.dispose()
        subscribe = favoriteFilmDao.getFavoriteFilmList().subscribe {
            _list.onNext(it)
        }
    }
}