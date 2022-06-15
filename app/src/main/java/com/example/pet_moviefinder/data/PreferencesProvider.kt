package com.example.pet_moviefinder.data

import android.content.Context

//Класс по работе с Preferences, сохраняет настройки приложения
//Должен уметь - сохранить настройку и получить настройку

class PreferencesProvider(context: Context) {
    private val appContext = context.applicationContext
    private val pref = appContext.getSharedPreferences(SettingType.PREFERENCES_NAME.key, Context.MODE_PRIVATE)

    fun getCategoryType(): String {
        return pref.getString(SettingType.CATEGORY_TYPE.key, CategoryTypes.POPULAR.key)!!
    }

    fun saveCategoryType(type: CategoryTypes) {
        pref.edit().putString(SettingType.CATEGORY_TYPE.key, type.key).apply()
    }

    enum class CategoryTypes(val key: String) {
        POPULAR("popular"),
        TOP("top_rated"),
        SOON("upcoming"),
        IN_CINEMA("now_playing");
    }

    private enum class SettingType(val key: String) {
        PREFERENCES_NAME("settings"),
        CATEGORY_TYPE("category")
    }
}