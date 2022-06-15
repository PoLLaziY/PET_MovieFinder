package com.example.pet_moviefinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding

    val viewModel = App.app.dagger.getFavoriteFragmentModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        //настройка и отрисовка окна фаворитов
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchInFocus.observe(viewLifecycleOwner) {
            if (it) binding.contentView.searchView.isIconified = false
            else {
                binding.contentView.searchView.clearFocus()
                viewModel.refreshFilmList()
            }
        }

        //настройка RecyclerView
        binding.contentView.rv.adapter = viewModel.adapter

        //настройка нижнего окна навигации
        binding.bottomBar.bottomNV.setOnItemSelectedListener { viewModel.onNavigationClickListener(it.itemId) }

        //настройка окна поиска
        //при нажатии открывается ввод
        binding.contentView.searchView.setOnClickListener {
            if (!viewModel.searchInFocus.value!!) viewModel.searchInFocus.value = true
        }

        //при закрытии убрать фокус
        binding.contentView.searchView.setOnCloseListener {
            viewModel.searchInFocus.value = false
            return@setOnCloseListener true
        }

        //добавление слушателя на окно поиска
        binding.contentView.searchView.setOnQueryTextListener(viewModel.onQueryTextListener)

        binding.contentView.swipeRefreshLayout.setOnRefreshListener {
            binding.contentView.swipeRefreshLayout.isRefreshing = true
            viewModel.refreshFilmList()
            binding.contentView.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFilmList()
    }
}
