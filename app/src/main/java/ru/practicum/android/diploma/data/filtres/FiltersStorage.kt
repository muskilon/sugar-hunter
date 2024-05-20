package ru.practicum.android.diploma.data.filtres

import android.content.SharedPreferences

class FiltersStorage(private val sharedPreferences: SharedPreferences) {

    fun getFilters(): String? {
        return sharedPreferences.getString(FILTERS, null)
    }

    fun updateFilters(filters: String?) {
        sharedPreferences.edit()
            .putString(FILTERS, filters)
            .apply()
    }
    companion object {
        const val FILTERS = "filters"
    }
}
