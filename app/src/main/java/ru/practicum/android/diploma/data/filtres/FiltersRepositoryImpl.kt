package ru.practicum.android.diploma.data.filtres

import com.google.gson.Gson
import ru.practicum.android.diploma.domain.FiltersRepository
import ru.practicum.android.diploma.domain.models.SavedFilters

class FiltersRepositoryImpl(
    private val filtersStorage: FiltresStorage
) : FiltersRepository {
    override fun getFilters(): SavedFilters? {
        return filtersStorage.getFitres()?.let {
            Gson().fromJson(it, SavedFilters::class.java)
        }
    }

    override fun updateFilters(filters: SavedFilters?) {
        return filtersStorage.updateFiltes(
            Gson().toJson(filters)
        )
    }
}
