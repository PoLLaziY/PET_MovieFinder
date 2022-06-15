package com.example.pet_moviefinder.model

import android.widget.Toast
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.data.PreferencesProvider
import com.example.pet_moviefinder.data.dao.FilmDao
import com.example.pet_moviefinder.data.dto.TmdbResultsDto
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.data.remote_api.TheMovieDbAPI
import com.example.pet_moviefinder.data.remote_api.TheMovieDbKey
import com.example.pet_moviefinder.data.repositories.FilmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface IFilmRepositoryController {
    fun updateData(onDataUpdate: ((repository: FilmRepository) -> Unit)? = null)
    fun refreshData(onDataRefresh: ((repository: FilmRepository) -> Unit)? = null)
    fun getList(): List<Film>
}

class FilmRepositoryController(
    val service: TheMovieDbAPI,
    val filmRepository: FilmRepository,
    val preferencesProvider: PreferencesProvider,
    val filmDao: FilmDao
) : IFilmRepositoryController {

    private var activePage = 0

    override fun updateData(onDataUpdate: ((repository: FilmRepository) -> Unit)?) {
        sendRequestToFilmList(++activePage) {
            if (it != null) {
                if (activePage == 1) filmRepository.clear()
                filmRepository.add(it.body()?.results)
                onDataUpdate?.invoke(filmRepository)
                CoroutineScope(Dispatchers.IO).launch {
                    if (activePage == 1) filmDao.clear()
                    val list = it.body()?.results
                    if (!list.isNullOrEmpty()) {
                        filmDao.insert(it.body()!!.results)
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    filmRepository.clear()
                    val list = filmDao.getFilmList()
                    CoroutineScope(Dispatchers.Main).launch {
                        filmRepository.add(list)
                        onDataUpdate?.invoke(filmRepository)
                        if (filmRepository.size() == 0) Toast.makeText(App.app, "Film Repository is empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun refreshData(onDataUpdate: ((repository: FilmRepository) -> Unit)?) {
        activePage = 0
        updateData(onDataUpdate)
    }

    override fun getList(): List<Film> {
        return filmRepository.getList()
    }

    private fun sendRequestToFilmList(
        page: Int,
        onResponse: (response: Response<TmdbResultsDto>?) -> Unit
    ) {
        service.getFilmList(
            preferencesProvider.getCategoryType(),
            TheMovieDbKey.get(),
            "ru-RU",
            page
        ).enqueue(
            object : Callback<TmdbResultsDto> {
                override fun onResponse(
                    call: Call<TmdbResultsDto>,
                    response: Response<TmdbResultsDto>
                ) {
                    onResponse.invoke(response)
                }

                override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                    onResponse.invoke(null)
                }
            }
        )
    }
}