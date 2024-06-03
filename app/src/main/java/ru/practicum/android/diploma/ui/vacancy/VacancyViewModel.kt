
package ru.practicum.android.diploma.ui.vacancy

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
    private val vacanciesInterActor: VacanciesInterActor,
) : ViewModel() {

    private var checkInFavouritesMutableLiveData = MutableLiveData<Boolean>()
    fun checkInFavouritesLiveData(): LiveData<Boolean> = checkInFavouritesMutableLiveData

    private var vacancyScreenState = MutableLiveData<VacancyFragmentState>(VacancyFragmentState.Start)
    fun getVacancyScreenStateLiveData(): LiveData<VacancyFragmentState> = vacancyScreenState

    init {
        viewModelScope.launch {
            checkIdInFavourites(vacancyId)

            if (checkInFavouritesMutableLiveData.value == true) {
                searchVacancyInBD(vacancyId)
            } else {
                searchVacancyById(vacancyId)
            }
        }
    }

    private suspend fun searchVacancyById(vacancyId: String) {
        vacancyScreenState.postValue(VacancyFragmentState.Loading)
        vacanciesInterActor
            .getVacancy(vacancyId)
            .collect { result ->
                processResult(result)
            }
    }

    private suspend fun searchVacancyInBD(vacancyId: String) {
        vacancyScreenState.postValue(VacancyFragmentState.Loading)
        vacancyScreenState.postValue(
            VacancyFragmentState.Content(
                favouriteDataBaseInteractor.getVacancyBiId(vacancyId)
            )
        )
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
                vacancyScreenState.postValue(VacancyFragmentState.Content(foundVacancy.value))
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
            withContext(Dispatchers.IO) {
                favouriteDataBaseInteractor.deleteFavouriteVacancy(
                    (vacancyScreenState.value as VacancyFragmentState.Content).vacancy
                )
                checkInFavouritesMutableLiveData.postValue(false)
            }
        }
    }
}
