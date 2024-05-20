
package ru.practicum.android.diploma.ui.vacancy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.vacancy.models.VacancyFragmentState

class VacancyViewModel(
    private val vacancyId: String,
    private val favouriteDataBaseInteractor: FavouriteDataBaseInteractor,
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    private var checkInFavouritesMutableLiveData = MutableLiveData<Boolean>()
    fun checkInFavouritesLiveData(): LiveData<Boolean> = checkInFavouritesMutableLiveData

    private var vacancyScreenState = MutableLiveData<VacancyFragmentState>(VacancyFragmentState.Start)
    fun getVacancyScreenStateLiveData(): LiveData<VacancyFragmentState> = vacancyScreenState

    init {
        viewModelScope.launch {
            searchVacancyById(vacancyId)
            checkIdInFavourites(vacancyId)
        }
    }

    private fun searchVacancyById(vacancyId: String) {
        viewModelScope.launch {
            vacancyScreenState.postValue(VacancyFragmentState.Loading)
            vacanciesInterActor
                .getVacancy(vacancyId)
                .collect { result ->
                    processResult(result)
                }

        }
    }

    private fun processResult(foundVacancy: Resource<VacancyDetails>) {
        when (foundVacancy) {
            is Resource.ConnectionError -> {
                vacancyScreenState.postValue(VacancyFragmentState.Error)
            }

            is Resource.NotFound -> {
                vacancyScreenState.postValue(VacancyFragmentState.Empty)
            }

            is Resource.Data -> {
                vacancyScreenState.postValue(VacancyFragmentState.Content(foundVacancy.value as VacancyDetails))
            }
        }
    }

    private suspend fun checkIdInFavourites(id: String) {
        withContext(Dispatchers.IO) {
            checkInFavouritesMutableLiveData.postValue(favouriteDataBaseInteractor.checkIdInFavourites(id))
        }
    }

    suspend fun likeOrDislikeButton() {
        if (checkInFavouritesMutableLiveData.value == true) {
            deleteFavouriteVacancy()
        } else {
            addFavouriteVacancy()
        }
    }

    private suspend fun addFavouriteVacancy() {
        if (vacancyScreenState.value is VacancyFragmentState.Content) {
            Log.d("add", "y")
            withContext(Dispatchers.IO) {
                favouriteDataBaseInteractor.addFavouriteVacancy(
                    (vacancyScreenState.value as VacancyFragmentState.Content).vacancy
                )
                checkInFavouritesMutableLiveData.postValue(true)
            }
        }
    }

    private suspend fun deleteFavouriteVacancy() {
        if (vacancyScreenState.value is VacancyFragmentState.Content) {
            Log.d("add", "n")
            withContext(Dispatchers.IO) {
                favouriteDataBaseInteractor.deleteFavouriteVacancy(
                    (vacancyScreenState.value as VacancyFragmentState.Content).vacancy
                )
                checkInFavouritesMutableLiveData.postValue(false)
            }
        }
    }
}
