package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyViewModel(
    private val vacancy: Vacancy,
    private val favouriteDataBaseInteractor: FavouriteDataBaseInteractor
) : ViewModel() {

    private var checkInFavouritesMutableLiveData = MutableLiveData<Boolean>()
    fun checkInFavourites(): LiveData<Boolean> = checkInFavouritesMutableLiveData

    init {
        viewModelScope.launch {
            checkIdInFavourites(vacancy)
        }
    }

    suspend fun checkIdInFavourites(vacancy: Vacancy) {
        viewModelScope.launch {
            checkInFavouritesMutableLiveData.postValue(favouriteDataBaseInteractor.checkIdInFavourites(vacancy.id))
        }
    }

    suspend fun addFavouriteVacancy(vacancy: Vacancy) {
        checkIdInFavourites(vacancy)
        favouriteDataBaseInteractor.addFavouriteVacancy(vacancy)
    }

    suspend fun deleteFavouriteVacancy(vacancy: Vacancy) {
        checkIdInFavourites(vacancy)
        favouriteDataBaseInteractor.deleteFavouriteVacancy(vacancy)
    }

}


