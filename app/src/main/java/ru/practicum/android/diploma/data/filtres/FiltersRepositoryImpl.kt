package ru.practicum.android.diploma.data.filtres

import com.google.gson.Gson
import ru.practicum.android.diploma.domain.FiltersRepository
import ru.practicum.android.diploma.domain.models.SavedFilters

class FiltersRepositoryImpl(
    private val filtersStorage: FiltersStorage
) : FiltersRepository {
    override fun getFilters(): SavedFilters {
        return filtersStorage.getFilters()?.let {
            Gson().fromJson(it, SavedFilters::class.java)
        } ?: SavedFilters(mutableMapOf())
    }

    override fun updateFilters(filters: SavedFilters?) {
        return filtersStorage.updateFilters(
            Gson().toJson(filters)
        )
    }
}
