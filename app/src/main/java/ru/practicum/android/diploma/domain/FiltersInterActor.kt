package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.SavedFilters

interface FiltersInterActor {
    fun getFilters(): SavedFilters?
    fun updateFilters(filters: SavedFilters?)
}
