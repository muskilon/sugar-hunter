package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel(
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchFragmentState>()
    fun observeState(): LiveData<SearchFragmentState> = stateLiveData

    private var latestSearchText: String = ""
    private var searchJob: Job? = null
    private var isClickAllowed = true

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                SearchFragmentState.Loading
            )
            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                processResult()
            }
        }
    }

    private fun processResult() {
        renderState(
            SearchFragmentState.Empty("Nothing found")
        )
    }

    private fun renderState(state: SearchFragmentState) {
        stateLiveData.postValue(state)
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

    fun searchVacancies(text: String) {
        viewModelScope.launch {
            vacanciesInterActor.searchVacancies(text).collect { result ->
                processResult(result)
            }
        }
    }

    //    ДЛЯ ТЕСТИРОВАНИЯ!!!
    fun getVacancy(id: String) {
        viewModelScope.launch {
            vacanciesInterActor.getVacancy(id).collect {
                when (it) {
                    is Resource.ConnectionError -> Log.d(TAG, it.message)

                    is Resource.NotFound -> Log.d(TAG, it.message)

                    is Resource.Data -> Log.d(TAG, it.value.toString())
                }
            }
        }
    }

    //        ДЛЯ ТЕСТИРОВАНИЯ!!!
    private fun processResult(foundVacancies: Resource<List<Vacancy>>) {
        when (foundVacancies) {
            is Resource.ConnectionError -> Log.d(TAG, foundVacancies.message)

            is Resource.NotFound -> Log.d(TAG, foundVacancies.message)

            is Resource.Data -> Log.d(TAG, foundVacancies.value.toString())
        }
    }

    companion object {
        private const val TAG = "process"
    }
}
