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

class FavouriteViewModel(
    private val favouriteDataBaseInteractor: FavouriteDataBaseInteractor
) : ViewModel() {

    private var stateMutableLiveData = MutableLiveData<FavouritesState>()
    fun checkStateLiveData(): LiveData<FavouritesState> = stateMutableLiveData

    private var favoriteMutableListLiveData = MutableLiveData<ArrayList<VacancyDetails>>()
    fun favoriteListLiveData(): LiveData<ArrayList<VacancyDetails>> = favoriteMutableListLiveData

    init {
        viewModelScope.launch {
            checkFavoriteList()
        }
    }

    fun checkFavoriteList() {
        viewModelScope.launch {
            favoriteMutableListLiveData.postValue(readFavoriteList())
        }
    }

    private suspend fun readFavoriteList(): ArrayList<VacancyDetails> {
        return withContext(Dispatchers.IO) {
            val flowList = favouriteDataBaseInteractor.getFavouritesVacancies()
            val favoriteList = ArrayList<VacancyDetails>()
            flowList.collect { vacansies ->
                favoriteList.addAll(vacansies)
            }
            favoriteList
        }
    }

    fun setStateContent() {
        viewModelScope.launch {
            val content = readFavoriteList()
            stateMutableLiveData.postValue(FavouritesState.Content(content))
        }
    }

    fun setStateEmpty() {
        stateMutableLiveData.postValue(FavouritesState.Empty)
    }

    fun setStateError() {
        stateMutableLiveData.postValue(FavouritesState.Error)
    }

}
