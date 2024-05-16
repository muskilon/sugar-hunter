package ru.practicum.android.diploma.domain.db

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavouriteDataBaseInteractorImpl(private val favouriteDataBaseRepository: FavouriteDataBaseRepository) :
    FavouriteDataBaseInteractor {
    override suspend fun addFavouriteVacancy(vacancy: VacancyDetails) {
        favouriteDataBaseRepository.addFavouriteVacancy(vacancy)
    }

    override suspend fun deleteFavouriteVacancy(vacancy: VacancyDetails) {
        favouriteDataBaseRepository.deleteFavouriteVacancy(vacancy)
    }

    override suspend fun checkIdInFavourites(id: String): Boolean {
        return favouriteDataBaseRepository.checkIdInFavourites(id)
    }

    override fun getFavouritesVacancies(): Flow<List<VacancyDetails>> {
        return favouriteDataBaseRepository.getFavouritesVacancies()
    }

}
