package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.SavedFiltres

interface FiltresInterActor {
    fun getFiltres(): SavedFiltres?
    fun updateFiltres(filtres: SavedFiltres)
}
