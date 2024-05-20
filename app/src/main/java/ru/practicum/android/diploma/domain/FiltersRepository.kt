package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.SavedFilters

interface FiltersRepository {
    fun getFilters(): SavedFilters?
    fun updateFilters(filters: SavedFilters?)
}
