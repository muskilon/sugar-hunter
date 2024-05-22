package ru.practicum.android.diploma.ui.filter.industry


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Resource

class ChooseIndustryViewModel(private val industryInteractor: IndustryInteractor) : ViewModel() {

    private var industryMutableListLiveData = MutableLiveData<ArrayList<Industries>>()
    fun industryListLiveData(): LiveData<ArrayList<Industries>> = industryMutableListLiveData

    init {
        viewModelScope.launch {
            getIndustries()
        }
    }

    suspend fun getIndustries() {
        val industriesList = ArrayList<Industries>()

        industryInteractor.getIndustries().collect { resource ->
            when (resource) {
                is Resource.Data -> {
                    industriesList.addAll(resource.value)
                    industryMutableListLiveData.postValue(industriesList)
                }

                is Resource.NotFound -> {}
                is Resource.ConnectionError -> {}
            }
        }

    }

}
