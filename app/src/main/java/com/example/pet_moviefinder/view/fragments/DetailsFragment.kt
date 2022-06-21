package com.example.pet_moviefinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.moviefinder.data.remote_api.TheMovieDbConst
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.view_model.DetailsFragmentModel
import com.example.pet_moviefinder.R
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.databinding.FragmentDetailsBinding

class DetailsFragment(val film: Film) : Fragment() {

    lateinit var binding: FragmentDetailsBinding
    val viewModel: DetailsFragmentModel by activityViewModels {
        App.app.dagger.provideDetailsModelFactory(
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.film.value = film

        viewModel.film.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.detailsTitle.text = viewModel.film.value!!.title
                binding.detailsDescription.text = viewModel.film.value!!.description
                Glide.with(binding.root.context)
                    .load(TheMovieDbConst.IMAGES_URL + "w342" + viewModel.film.value!!.iconUrl)
                    .centerCrop()
                    .into(binding.detailsPoster)
                binding.favoriteFab.setImageResource(
                    if (viewModel.isFavorite.value!!) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) {
            if (it) binding.favoriteFab.setImageResource(R.drawable.ic_baseline_favorite_24)
            else binding.favoriteFab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }

        //слушатели для кнопок
        binding.favoriteFab.setOnClickListener {
            viewModel.isFavorite.value = !viewModel.isFavorite.value!!
        }

        binding.shareFab.setOnClickListener {
            viewModel.onShareButtonClick()
        }
    }
}