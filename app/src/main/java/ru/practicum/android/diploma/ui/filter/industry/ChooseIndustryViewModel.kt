package ru.practicum.android.diploma.ui.filter.industry


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.IndustryState
import ru.practicum.android.diploma.domain.models.Resource

class ChooseIndustryViewModel(private val industryInteractor: IndustryInteractor) : ViewModel() {

    private var industryMutableListLiveData = MutableLiveData<ArrayList<Industries>>()
    fun industryListLiveData(): LiveData<ArrayList<Industries>> = industryMutableListLiveData

    private var stateMutableLiveData = MutableLiveData<IndustryState>()
    fun checkStateLiveData() : LiveData<IndustryState> = stateMutableLiveData

    init {
        viewModelScope.launch {
            updateState()
        }
    }

    suspend fun updateState() {
        val industriesList = ArrayList<Industries>()

        industryInteractor.getIndustries().collect { resource ->
            when (resource) {
                is Resource.Data -> {
                    industriesList.addAll(resource.value)
                    stateMutableLiveData.postValue(IndustryState.Content(industriesList))
                }
                is Resource.NotFound -> {
                    stateMutableLiveData.postValue(IndustryState.NotFound)
                }
                is Resource.ConnectionError -> {
                    stateMutableLiveData.postValue(IndustryState.ConnectionError)
                }
            }
        }
    }

    fun getProgressBar(){
        stateMutableLiveData.postValue(IndustryState.Loading)
    }

}
