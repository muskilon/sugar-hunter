package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.FiltersRepository
import ru.practicum.android.diploma.domain.models.SavedFilters

class FiltersInterActorImpl(
    private val repository: FiltersRepository
) : FiltersInterActor {
    override fun getFilters(): SavedFilters {
        return repository.getFilters()
    }

    override fun updateFilters(filters: SavedFilters?) {
        repository.updateFilters(filters)
    }
}
