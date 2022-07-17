package com.example.pet_moviefinder.view_model

import androidx.lifecycle.ViewModel
import com.example.pet_moviefinder.model.IFilmRepositoryController
import com.example.pet_moviefinder.model.INavigationController
import com.example.pet_moviefinder.R
import com.example.pet_moviefinder.data.PreferencesProvider
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsFragmentModel(
    val preferencesProvider: PreferencesProvider,
    val repositoryController: IFilmRepositoryController,
    val navigation: INavigationController
): ViewModel() {

    val onNavigationClickListener: (itemId: Int) -> Boolean = {
        navigation.onNavigationClick(it)
        true
    }

    val categoryType = MutableStateFlow(preferencesProvider.getCategoryType())

    fun onCategoryTypeChanged(buttonId: Int) {
        var type = PreferencesProvider.CategoryTypes.POPULAR
        when (buttonId) {
            R.id.popular -> type = PreferencesProvider.CategoryTypes.POPULAR
            R.id.top -> type = PreferencesProvider.CategoryTypes.TOP
            R.id.soon -> type = PreferencesProvider.CategoryTypes.SOON
            R.id.inCinema -> type = PreferencesProvider.CategoryTypes.IN_CINEMA
        }
        categoryType.value = type.key
        preferencesProvider.saveCategoryType(type)
        repositoryController.refreshData(null)
    }
}