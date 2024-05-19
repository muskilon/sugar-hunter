package ru.practicum.android.diploma.ui.favourite

import android.util.Log
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
    fun favoriteListLiveData(): LiveData<ArrayList<VacancyDetails>> = favoriteMutableListLiveData

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
            flowList.collect { vacansies ->
                favoriteList.addAll(vacansies)
            }
            Log.d("list", favoriteList.toString())
            favoriteMutableListLiveData.postValue(favoriteList)
        }
    }

    fun updateState() {
        when (favoriteListLiveData().value){
            null -> setStateError()
            emptyList<VacancyDetails>() -> setStateEmpty()
            else -> setStateContent()
        }
    }



    fun setStateContent() {
        viewModelScope.launch {
//            val content = readFavoriteList()
            if (favoriteMutableListLiveData.value != null){
                stateMutableLiveData.postValue(FavouritesState.Content(favoriteMutableListLiveData.value!!))
                Log.d("content", stateMutableLiveData.toString())
            } else {
                setStateError()
            }
        }
    }

    fun setStateEmpty() {
        stateMutableLiveData.postValue(FavouritesState.Empty)
    }

    fun setStateError() {
        stateMutableLiveData.postValue(FavouritesState.Error)
    }

}
