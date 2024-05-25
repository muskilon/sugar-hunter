package ru.practicum.android.diploma.ui.filter.industry


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.IndustryInteractor
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.IndustryState
import ru.practicum.android.diploma.domain.models.Resource
import java.util.Locale

class ChooseIndustryViewModel(private val industryInteractor: IndustryInteractor) : ViewModel() {

    private var latestSearchText = ""
    private var searchJob: Job? = null
    private var industriesList = ArrayList<Industries>()

    private var stateMutableLiveData = MutableLiveData<IndustryState>()
    fun checkStateLiveData(): LiveData<IndustryState> = stateMutableLiveData

    init {
        viewModelScope.launch {
            renderState()
        }
    }

    private suspend fun renderState() {
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

    fun searchDebounce(text: String) {
        if (latestSearchText == text || text.isEmpty()) {
            searchJob?.cancel()
            viewModelScope.launch {
                renderState()
            }
        } else {
            getProgressBar()
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                sortIndustriesListByInput(text)
            }
        }
    }

    private fun sortIndustriesListByInput(request: String) {
        if (request.isNotEmpty()) {
            val filteredList = industriesList.filter {
                it.name?.lowercase(Locale.getDefault())?.startsWith(request) == true ||
                    it.name?.lowercase(Locale.getDefault())?.contains(request) == true
            }
            val sortedList = filteredList.sortedWith(compareBy(
                { it.name?.startsWith(request, ignoreCase = true) == false },
                { it.name }
            ))
            val sortedArrayList = ArrayList(sortedList)
            if (sortedArrayList.isEmpty()) {
                stateMutableLiveData.postValue(IndustryState.NotFound)
            } else {
                stateMutableLiveData.postValue(IndustryState.Content(sortedArrayList))
            }
        }
    }

    private fun getProgressBar() {
        stateMutableLiveData.postValue(IndustryState.Loading)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}
