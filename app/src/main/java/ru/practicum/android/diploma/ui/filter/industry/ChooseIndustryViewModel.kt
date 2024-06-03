package ru.practicum.android.diploma.ui.filter.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.IndustryState
import ru.practicum.android.diploma.domain.models.Resource

class ChooseIndustryViewModel(private val industryInteractor: IndustryInteractor) : ViewModel() {

    private var industriesList = ArrayList<Industry>()

    private var stateMutableLiveData = MutableLiveData<IndustryState>()
    fun checkStateLiveData(): LiveData<IndustryState> = stateMutableLiveData

    init {
        viewModelScope.launch {
            renderState()
        }
    }

    private suspend fun renderState() {
        stateMutableLiveData.postValue(IndustryState.Loading)
        industryInteractor.getIndustries().collect { resource ->
            when (resource) {
                is Resource.Data -> {
                    industriesList.clear()
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

    fun searchIndustry(request: String) {
        if (request.isNotEmpty()) {
            val result = industriesList.filter {
                it.name.contains(request, true)
            }
            if (result.isEmpty()) {
                stateMutableLiveData.postValue(IndustryState.NotFound)
            } else {
                stateMutableLiveData.postValue(IndustryState.Content(result))
            }
        } else {
            stateMutableLiveData.postValue(IndustryState.Content(industriesList))
        }
    }
}
