package ru.practicum.android.diploma.app

import android.app.Application
import android.content.res.Configuration
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
        startKoin {
            androidContext(this@App)
            modules(dataModules, domainModules, viewModelModules)
        }
        appResource = resources

        setAppTheme()

    }

    private fun setAppTheme() {
        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {

            Configuration.UI_MODE_NIGHT_YES -> {
                changeTheme(true)
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                changeTheme(false)
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                changeTheme(false)
            }
        }
    }

    companion object {
        private var appResource: Resources? = null

        fun changeTheme(isNightMode: Boolean) {
            when (isNightMode) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        fun getAppResource(): Resources? { // Это если где-то нужно будет получить ресурсы
            return appResource
        }
    }
}
