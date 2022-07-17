package com.example.pet_moviefinder.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.data.PreferencesProvider
import com.example.pet_moviefinder.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    val viewModel = App.app.dagger.getSettingsFragmentModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            viewModel.onCategoryTypeChanged(i)
        }

        lifecycleScope.launch {
            viewModel.categoryType.collect {
                checkTypeRadio(it)
            }
        }

        //настройка нижнего окна навигации
        binding.bottomBar.bottomNV.setOnItemSelectedListener { viewModel.onNavigationClickListener(it.itemId) }

    }

    private fun checkTypeRadio(type: String) {
        var button = binding.popular
        when (type) {
            PreferencesProvider.CategoryTypes.POPULAR.key -> button = binding.popular
            PreferencesProvider.CategoryTypes.TOP.key -> button = binding.top
            PreferencesProvider.CategoryTypes.SOON.key -> button = binding.soon
            PreferencesProvider.CategoryTypes.IN_CINEMA.key -> button = binding.inCinema
        }
        button.isChecked = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}