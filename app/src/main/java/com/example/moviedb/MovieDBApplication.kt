package com.example.moviedb

import android.app.Application
import com.example.moviedb.database.AppContainer
import com.example.moviedb.database.DefaultAppContainer

class MovieDBApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}