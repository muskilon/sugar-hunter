package ru.practicum.android.diploma.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.dataModules
import ru.practicum.android.diploma.di.domainModules
import ru.practicum.android.diploma.di.viewModelModules

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(dataModules, domainModules, viewModelModules)
        }

    }
}
