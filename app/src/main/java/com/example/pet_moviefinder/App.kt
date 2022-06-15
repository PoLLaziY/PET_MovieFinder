package com.example.pet_moviefinder

import android.app.Application
import com.example.pet_moviefinder.di.DaggerAppComponent

class App() : Application() {

    var dagger = DaggerAppComponent.factory().create(this)

    init {
        app = this
    }

    companion object {
        lateinit var app: App
            private set
    }
}