package ru.practicum.android.diploma.app

import android.app.Application
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.dataModules
import ru.practicum.android.diploma.di.domainModules
import ru.practicum.android.diploma.di.viewModelModules

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appResources = resources
        startKoin {
            androidContext(this@App)
            modules(dataModules, domainModules, viewModelModules)
        }

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
    }
    companion object {
        private lateinit var appResources: Resources
        fun getAppResources(): Resources {
            return appResources
        }
    }
}
