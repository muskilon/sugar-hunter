package ru.practicum.android.diploma.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.AreaItem
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState

class SearchViewModel(
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchFragmentState>()
    private val foundAreas = mutableListOf<AreaItem>() // Для тестирования
    fun observeState(): LiveData<SearchFragmentState> = stateLiveData

    private var latestSearchText: String = ""
    private var searchJob: Job? = null
    private var isClickAllowed = true

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val TAG = "process"
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

    fun searchVacancies(options: Map<String, String>) {
        renderState(
            SearchFragmentState.Loading
        )
        viewModelScope.launch {
            vacanciesInterActor
                .searchVacancies(options)
                .collect { result ->
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

    fun getIndustries() {
        viewModelScope.launch {
            vacanciesInterActor.getIndustries().collect {
                when (it) {
                    is Resource.ConnectionError -> Log.d(TAG, it.message)

                    is Resource.NotFound -> Log.d(TAG, it.message)

                    is Resource.Data -> Log.d(TAG, it.value.toString())
                }
            }
        }
    }

    fun getAreas() {
        viewModelScope.launch {
            vacanciesInterActor.getAreaDictionary().collect {
                when (it) {
                    is Resource.ConnectionError -> Log.d(TAG, it.message)

                    is Resource.NotFound -> Log.d(TAG, it.message)

                    is Resource.Data -> {
                        val areas = it.value.container
                        foundAreas.clear()
                        areas.getArea("Мос")
                        Log.d("FILTER", foundAreas.toString())
                    }
                }
            }
        }
    }

    private fun List<AreaItem>.getArea(name: String): AreaItem? {
        for (area in this) {
            if (area.name.startsWith(name, true)) {
                foundAreas.add(area)
            }
            val found = area.areas?.getArea(name)
            if (found != null) {
                return found
            }
        }
        return null
    }

    fun searchDebounce(changedText: String, options: Map<String, String>) {
        if (latestSearchText.equals(changedText) || changedText.isEmpty()) {
            return
        }

        this.latestSearchText = changedText

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchVacancies(options)
        }
    }

    private fun processResult(foundVacancies: Resource<VacanciesResponse>) {
        when (foundVacancies) {
            is Resource.ConnectionError -> {
                renderState(
                    SearchFragmentState.Error(
                        foundVacancies.message
                    )
                )
            }

            is Resource.NotFound -> {
                renderState(
                    SearchFragmentState.Empty(
                        foundVacancies.message
                    )
                )
            }

            is Resource.Data -> {
                renderState(
                    SearchFragmentState.Content(
                        foundVacancies.value
                    )
                )
            }
        }
    }
}
