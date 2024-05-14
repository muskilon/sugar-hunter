package ru.practicum.android.diploma.domain.db

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavouriteDataBaseRepository {

    suspend fun addFavouriteVacancy(vacancy: Vacancy)

    suspend fun deleteFavouriteVacancy(vacancy: Vacancy)

    suspend fun checkIdInFavourites(id: String): Boolean

    fun getFavouritesVacancies(): Flow<List<Vacancy>>

}
