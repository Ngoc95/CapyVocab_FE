package com.example.capyvocab_fe

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
