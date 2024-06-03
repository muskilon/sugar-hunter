package ru.practicum.android.diploma.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor
import ru.practicum.android.diploma.domain.models.FavouritesState
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavouriteViewModel(private val favouriteDataBaseInteractor: FavouriteDataBaseInteractor) : ViewModel() {

    private var stateMutableLiveData = MutableLiveData<FavouritesState>(FavouritesState.Empty)
    fun checkStateLiveData(): LiveData<FavouritesState> = stateMutableLiveData

    private var favoriteMutableListLiveData = MutableLiveData<ArrayList<VacancyDetails>>()
    private fun favoriteListLiveData(): LiveData<ArrayList<VacancyDetails>> = favoriteMutableListLiveData

    init {
        viewModelScope.launch {
            readFavoriteList()
            updateState()
        }
    }

    suspend fun readFavoriteList() {
        return withContext(Dispatchers.IO) {
            val flowList = favouriteDataBaseInteractor.getFavouritesVacancies()
            val favoriteList = ArrayList<VacancyDetails>()
            flowList.collect { vacancies ->
                favoriteList.addAll(vacancies)
            }
            favoriteMutableListLiveData.postValue(favoriteList)
        }
    }

    fun updateState() {
        when (favoriteListLiveData().value) {
            null -> setStateError()
            emptyList<VacancyDetails>() -> setStateEmpty()
            else -> setStateContent()
        }
    }

    private fun setStateContent() {
        viewModelScope.launch {
            if (favoriteMutableListLiveData.value != null) {
                stateMutableLiveData.postValue(FavouritesState.Content(favoriteMutableListLiveData.value!!))
            } else {
                setStateError()
            }
        }
    }

    private fun setStateEmpty() {
        stateMutableLiveData.postValue(FavouritesState.Empty)
    }

    private fun setStateError() {
        stateMutableLiveData.postValue(FavouritesState.Error)
    }
}
