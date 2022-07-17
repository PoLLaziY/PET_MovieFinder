package com.example.pet_moviefinder.view

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.example.pet_moviefinder.App
import com.example.pet_moviefinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var navigationInterface = App.app.dagger.getNavigationInterface()

    lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        navigationInterface.activity = this
    }

    override fun onDestroy() {
        navigationInterface.activity = null
        super.onDestroy()
    }
}