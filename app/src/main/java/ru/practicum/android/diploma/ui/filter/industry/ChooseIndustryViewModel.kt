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

class ChooseIndustryViewModel(private val industryInteractor: IndustryInteractor) : ViewModel() {

    private var latestSearchText = ""
    private var searchJob: Job? = null
    private var isClickAllowed = true
    private var currentIndustry = ArrayList<Industries>()

   // private var industryMutableListLiveData = MutableLiveData<ArrayList<Industries>>()
  //  fun industryListLiveData(): LiveData<ArrayList<Industries>> = industryMutableListLiveData

    private var stateMutableLiveData = MutableLiveData<IndustryState>()
    fun checkStateLiveData(): LiveData<IndustryState> = stateMutableLiveData

    init {
        viewModelScope.launch {
            renderState()
        }
    }

    suspend fun renderState() {
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

    fun getProgressBar() {
        stateMutableLiveData.postValue(IndustryState.Loading)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun searchDebounce(text: String) {
        if (latestSearchText == text || text.isEmpty()) {
            searchJob?.cancel()
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchIndustry(text)
            }
        }
    }

    fun searchIndustry(request: String) {
        if (request.isNotEmpty()){
            getProgressBar()
            //todo поиск
        }
    }

    private fun processResult(industries: ArrayList<Industries>?, error: String?) {
        val currentIndustry = ArrayList<Industries>()
        if (industries != null && industries.size != 0){
            currentIndustry.clear()
            currentIndustry.addAll(industries)
            stateMutableLiveData.postValue(IndustryState.Content(currentIndustry))
        } else if (error == OFFLINE) {
            stateMutableLiveData.postValue(IndustryState.ConnectionError)
        } else {
            stateMutableLiveData.postValue(IndustryState.NotFound)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val OFFLINE = "Проверьте подключение к интернету"
    }

}
