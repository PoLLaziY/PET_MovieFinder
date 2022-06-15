package com.example.pet_moviefinder.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var navigationInterface = App.app.dagger.getNavigationInterface()
    var dataUpdateInterface = App.app.dagger.getDataUpdateInterface()
    var favoriteRepositoryInterface = App.app.dagger.getFavoriteRepositoryInterface()

    lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        navigationInterface.activity = this

        dataUpdateInterface.updateData()
        favoriteRepositoryInterface.refreshData()

    }

    override fun onDestroy() {
        navigationInterface.activity = null
        super.onDestroy()
    }
}