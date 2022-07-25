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
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface IFilmRepositoryController {
    fun updateData(onDataUpdate: ((repository: FilmRepository) -> Unit)? = null)
    fun refreshData(onDataRefresh: ((repository: FilmRepository) -> Unit)? = null)
    fun getFilmList(): BehaviorSubject<List<Film>>
}

class FilmRepositoryController(
    val service: TheMovieDbAPI,
    val filmRepository: FilmRepository,
    val preferencesProvider: PreferencesProvider
) : IFilmRepositoryController {

    private var activePage = 0

    override fun updateData(onDataUpdate: ((repository: FilmRepository) -> Unit)?) {
        sendRequestToFilmList(++activePage) {
            if (it != null) {
                it.body()?.results?.forEach { film ->
                    filmRepository.insert(film)
                }
                filmRepository.refresh()
                onDataUpdate?.invoke(filmRepository)
                Toast.makeText(App.app, "Date update", Toast.LENGTH_SHORT).show()
            } else {
                onDataUpdate?.invoke(filmRepository)
                Toast.makeText(App.app, "Date not update", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun refreshData(onDataUpdate: ((repository: FilmRepository) -> Unit)?) {
        activePage = 0
        updateData(onDataUpdate)
    }

    override fun getFilmList(): BehaviorSubject<List<Film>> {
        return filmRepository.getFilmList()
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