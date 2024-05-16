package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacancyViewModel(
    private val vacancy: VacancyDetails,
    private val favouriteDataBaseInteractor: FavouriteDataBaseInteractor
) : ViewModel() {

    private var checkInFavouritesMutableLiveData = MutableLiveData<Boolean>()
    fun checkInFavouritesLiveData(): LiveData<Boolean> = checkInFavouritesMutableLiveData

    init {
        viewModelScope.launch {
            checkIdInFavourites(vacancy)
        }
    }

    suspend fun checkIdInFavourites(vacancy: VacancyDetails) {
        viewModelScope.launch {
            checkInFavouritesMutableLiveData.postValue(favouriteDataBaseInteractor.checkIdInFavourites(vacancy.id))
        }
    }

    suspend fun addFavouriteVacancy(vacancy: VacancyDetails) {
        checkIdInFavourites(vacancy)
        favouriteDataBaseInteractor.addFavouriteVacancy(vacancy)
    }

    suspend fun deleteFavouriteVacancy(vacancy: VacancyDetails) {
        checkIdInFavourites(vacancy)
        favouriteDataBaseInteractor.deleteFavouriteVacancy(vacancy)
    }

}
