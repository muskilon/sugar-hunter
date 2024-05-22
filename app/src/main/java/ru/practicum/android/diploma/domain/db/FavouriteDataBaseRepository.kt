package ru.practicum.android.diploma.domain.db

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface FavouriteDataBaseRepository {

    suspend fun addFavouriteVacancy(vacancy: VacancyDetails)

    suspend fun deleteFavouriteVacancy(vacancy: VacancyDetails)

    suspend fun checkIdInFavourites(id: String): Boolean

    fun getFavouritesVacancies(): Flow<List<VacancyDetails>>

}
