package ru.practicum.android.diploma.ui.vacancy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState

class VacancyViewModel(
    private val vacancyId: String,
    private val favouriteDataBaseInteractor: FavouriteDataBaseInteractor,
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    private var checkInFavouritesMutableLiveData = MutableLiveData<Boolean>()
    fun checkInFavouritesLiveData(): LiveData<Boolean> = checkInFavouritesMutableLiveData

    private var vacancyMutableLiveData = MutableLiveData<VacancyDetails>()
    fun getVacancyLiveData(): LiveData<VacancyDetails> = vacancyMutableLiveData

    init {
        viewModelScope.launch {
            searchVacancyById(vacancyId)

//            checkIdInFavourites(vacancy)

        }
    }


    fun searchVacancyById(vacancyId: String) {
        viewModelScope.launch {
            vacanciesInterActor
                .getVacancy(vacancyId)
                .collect { result ->
                    processResult(result)
                }

        }
    }

    fun processResult(foundVacance: Resource<VacancyDetails>) {
        when (foundVacance) {
            is Resource.ConnectionError -> {
                //Обработай ошибки
                        foundVacance.message
            }

            is Resource.NotFound -> {
                //Обработай ошибки
                        foundVacance.message
            }

            is Resource.Data -> {
                //Проверь внимательно
                Log.d("vacancy", foundVacance.value.toString())
                        vacancyMutableLiveData.postValue(foundVacance.value as VacancyDetails)
            }
        }
    }

    suspend fun checkIdInFavourites(id: String) {
        viewModelScope.launch {
            checkInFavouritesMutableLiveData.postValue(favouriteDataBaseInteractor.checkIdInFavourites(id))
        }
    }

    suspend fun addFavouriteVacancy(vacancy: VacancyDetails) {
//        checkIdInFavourites(vacancy)
        favouriteDataBaseInteractor.addFavouriteVacancy(vacancy)
    }

    suspend fun deleteFavouriteVacancy(vacancy: VacancyDetails) {
//        checkIdInFavourites(vacancy)
        favouriteDataBaseInteractor.deleteFavouriteVacancy(vacancy)
    }

}
