package com.pyasotrubley.vegs

import android.app.Application
import com.pyasotrubley.vegs.data.SettingsRepository

class PyasotRubleyApp : Application() {
    lateinit var settingsRepository: SettingsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        settingsRepository = SettingsRepository(this)
    }
}
