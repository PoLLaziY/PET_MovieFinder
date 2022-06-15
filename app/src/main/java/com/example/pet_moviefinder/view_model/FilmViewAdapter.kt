package com.example.pet_moviefinder.view_model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviefinder.data.remote_api.TheMovieDbConst
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.databinding.FilmItemBinding
import kotlinx.coroutines.*

class FilmViewAdapter(val filmItemClickListener: ((film: Film) -> Unit)?) :
    RecyclerView.Adapter<FilmViewAdapter.FilmViewHolder>() {

    var list: List<Film> = emptyList()
        set(value) {
            field = value
            CoroutineScope(Dispatchers.Main).launch {
                notifyDataSetChanged()
            }

        }

    var doOnListFinished: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //привязка данных из базы к элементам RecyclerView
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                //привязка данных из базы к View
                holder.film = list[position]
                if (position == list.size - 6) doOnListFinished.invoke()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class FilmViewHolder(val binding: FilmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var film: Film? = null
            set(value) {
                if (value != null) {
                    field = value
                    bind(value)
                    binding.root.setOnClickListener {
                        if (film != null) {
                            filmItemClickListener?.invoke(film!!)
                        }
                    }
                }
            }

        fun bind(film: Film) {
            binding.title.text = film.title
            binding.description.text = film.description
            Glide.with(binding.root.context)
                .load(TheMovieDbConst.IMAGES_URL + "w342" + film.iconUrl)
                .centerCrop()
                .into(binding.actionImage)
            binding.rating.rating = (film.rating?.toFloat() ?: 0f) * 10
        }
    }
}
