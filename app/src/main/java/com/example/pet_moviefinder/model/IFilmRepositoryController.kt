package com.example.pet_moviefinder.model

import android.widget.Toast
import androidx.lifecycle.LiveData
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
    fun getLiveData(): LiveData<List<Film>>
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
                onDataUpdate?.invoke(filmRepository)
                CoroutineScope(Dispatchers.IO).launch {
                    val list = it.body()?.results
                    if (!list.isNullOrEmpty()) {
                        if (activePage == 1) filmDao.clear()
                        filmDao.insert(it.body()!!.results)
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val list = filmDao.getFilmList()
                    CoroutineScope(Dispatchers.Main).launch {
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

    override fun getLiveData(): LiveData<List<Film>> {
        return filmRepository.getLiveData()
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