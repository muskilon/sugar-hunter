package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.FiltresInterActor
import ru.practicum.android.diploma.domain.FiltresRepository
import ru.practicum.android.diploma.domain.models.SavedFiltres

class FiltresInterActorImpl(
    private val repository: FiltresRepository
) : FiltresInterActor {
    override fun getFiltres(): SavedFiltres? {
        return repository.getFiltres()
    }

    override fun updateFiltres(filtres: SavedFiltres) {
        repository.updateFiltres(filtres)
    }
}
