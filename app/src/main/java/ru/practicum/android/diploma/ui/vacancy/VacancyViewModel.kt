package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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

//            checkIdInFavourites(vacancy)

        }
    }

    fun searchVacancyById(vacancyId: String) {
        viewModelScope.launch {
            vacancyScreenState.postValue(VacancyFragmentState.Loading)
            vacanciesInterActor
                .getVacancy(vacancyId)
                .collect { result ->
                    processResult(result)
                }

        }
    }

    fun processResult(foundVacancy: Resource<VacancyDetails>) {
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
