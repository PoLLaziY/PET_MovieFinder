package com.example.pet_moviefinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.view_model.HomeFragmentModel
import com.example.pet_moviefinder.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    var viewModel: HomeFragmentModel = App.app.dagger.getHomeModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFilmList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchInFocus.observe(viewLifecycleOwner) {
            if (it) binding.contentView.searchView.isIconified = false
            else binding.contentView.searchView.clearFocus()

        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.contentView.swipeRefreshLayout.isRefreshing = it
        }

        //настройка RecyclerView
        binding.contentView.rv.adapter = viewModel.adapter

        //настройка кнопки МЕНЮ верхней навигационной панели
        binding.appBar.appNB.setNavigationOnClickListener { viewModel.onNavigationClickListener(it.id) }

        //Настройка кнопки НАСТРОЙКИ верхней навигационной панели
        binding.appBar.appNB.setOnMenuItemClickListener { viewModel.onNavigationClickListener(it.itemId) }

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

        //настройка свайпа для обновления
        binding.contentView.swipeRefreshLayout.setOnRefreshListener {
            viewModel.isRefreshing.value = true
        }
    }
}