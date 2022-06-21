package com.example.pet_moviefinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.data.entity.Film
import com.example.pet_moviefinder.view_model.HomeFragmentModel
import com.example.pet_moviefinder.databinding.FragmentHomeBinding
import com.example.pet_moviefinder.view_model.FilmViewAdapter

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val viewModel: HomeFragmentModel by activityViewModels {
        App.app.dagger.provideHomeModelFactory(
            this
        )
    }
    private var adapter = FilmViewAdapter() { film: Film -> viewModel.onFilmItemClick(film) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //настройка RecyclerView
        binding.contentView.rv.adapter = adapter

        viewModel.listData.observe(viewLifecycleOwner) {
            adapter.list = it
        }

        binding.contentView.rv.addOnScrollListener(viewModel.rvScrollListener)

        binding.contentView.rv.scrollToPosition(if (viewModel.scrollState < adapter.list?.size ?: 0) viewModel.scrollState else 0)

        viewModel.searchInFocus.observe(viewLifecycleOwner) {
            if (it) binding.contentView.searchView.isIconified = false
            else {
                binding.contentView.searchView.clearFocus()
                adapter.list = viewModel.listData.value
            }
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.contentView.swipeRefreshLayout.isRefreshing = it
            if (it) viewModel.refreshData()
        }

        //настройка кнопки МЕНЮ верхней навигационной панели
        binding.appBar.appNB.setNavigationOnClickListener { viewModel.onNavigationClickListener(it.id) }

        //Настройка кнопки НАСТРОЙКИ верхней навигационной панели
        binding.appBar.appNB.setOnMenuItemClickListener { viewModel.onNavigationClickListener(it.itemId) }

        //настройка нижнего окна навигации
        binding.bottomBar.bottomNV.setOnItemSelectedListener {
            viewModel.onNavigationClickListener(
                it.itemId
            )
        }

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
        binding.contentView.searchView.setOnQueryTextListener(viewModel.onQueryTextListener(adapter))

        //настройка свайпа для обновления
        binding.contentView.swipeRefreshLayout.setOnRefreshListener {
            viewModel.isRefreshing.value = true
        }
    }
}