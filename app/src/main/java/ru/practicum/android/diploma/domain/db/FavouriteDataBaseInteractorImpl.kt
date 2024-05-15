package ru.practicum.android.diploma.domain.db

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.FavouriteDataBaseRepositoryImpl
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouriteDataBaseInteractorImpl(
    private val favouriteDataBaseRepository: FavouriteDataBaseRepository
) : FavouriteDataBaseInteractor {
    override suspend fun addFavouriteVacancy(vacancy: Vacancy) {
        favouriteDataBaseRepository.addFavouriteVacancy(vacancy)
    }

    override suspend fun deleteFavouriteVacancy(vacancy: Vacancy) {
        favouriteDataBaseRepository.deleteFavouriteVacancy(vacancy)
    }

    override suspend fun checkIdInFavourites(id: String): Boolean {
        return favouriteDataBaseRepository.checkIdInFavourites(id)
    }

    override fun getFavouritesVacancies(): Flow<List<Vacancy>> {
        return favouriteDataBaseRepository.getFavouritesVacancies()
    }

}
