package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.SavedFiltres

interface FiltresRepository {
    fun getFiltres(): SavedFiltres?
    fun updateFiltres(filtres: SavedFiltres)
}
