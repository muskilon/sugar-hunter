package ru.practicum.android.diploma.data.filtres

import com.google.gson.Gson
import ru.practicum.android.diploma.domain.FiltresRepository
import ru.practicum.android.diploma.domain.models.SavedFiltres

class FiltresRepositoryImpl(
    private val filtresStorage: FiltresStorage
) : FiltresRepository {
    override fun getFiltres(): SavedFiltres? {
        return filtresStorage.getFitres()?.let {
            Gson().fromJson(it, SavedFiltres::class.java)
        }
    }

    override fun updateFiltres(filtres: SavedFiltres) {
        return filtresStorage.updateFiltes(
            Gson().toJson(filtres)
        )
    }
}
