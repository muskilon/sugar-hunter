package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.models.SavedFilters

class FilterViewModel(
    val filtersInterActor: FiltersInterActor
) : ViewModel() {
    fun getFilters(): MutableMap<String, String> {
        return filtersInterActor.getFilters().filters
    }

    fun updateFilters(filters: MutableMap<String, String>) {
        filtersInterActor.updateFilters(SavedFilters(filters))
    }
}
