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
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState

class SearchViewModel(
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchFragmentState>()
    private val foundAreas = mutableListOf<AreaItem>() // Для тестирования
    private var currentPage = 0
    private var totalPages = 0
    private var latestSearchText = ""
    private var searchJob: Job? = null
    private var isClickAllowed = true
    private var currentVacancies = listOf<Vacancy>()
    var isPageLoading = false

    fun observeState(): LiveData<SearchFragmentState> = stateLiveData
    private fun renderState(state: SearchFragmentState) {
        stateLiveData.postValue(state)
    }
    fun getSearchRequest(text: String, page: String?): HashMap<String, String> {
        val request: HashMap<String, String> = HashMap()
        with(request) {
            this[TEXT] = text
            if (!page.isNullOrEmpty()) {
                this[PAGE] = page
            }
            this[PER_PAGE] = PAGE_SIZE
        }

        return request
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
                searchVacancies(getSearchRequest(text, null))
            }
        }
    }

    fun searchVacancies(request: Map<String, String>) {
        request[TEXT]?.let {text ->
            if (latestSearchText == text || text.isEmpty()) {
                searchJob?.cancel()
            } else {
                renderState(
                    SearchFragmentState.Loading
                )
                currentVacancies = listOf()
                latestSearchText = text
                viewModelScope.launch {
                    vacanciesInterActor
                        .searchVacancies(request)
                        .collect { result ->
                            processResult(result)
                        }
                }
            }
        }
    }

    fun onLastItemReached() {
        if (currentPage < totalPages - 1) {
            currentPage++
            isPageLoading = true
            viewModelScope.launch {
                vacanciesInterActor
                    .searchVacancies(getSearchRequest(latestSearchText, currentPage.toString()))
                    .collect { result ->
                        processResult(result)
                    }
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
                var data = foundVacancies.value
                totalPages = foundVacancies.value.pages
                currentPage = foundVacancies.value.page
                if (currentPage != 0) {
                    currentVacancies = currentVacancies + data.items
                    data = data.copy(items = currentVacancies)
                } else {
                    currentVacancies = data.items
                }
                renderState(
                    SearchFragmentState.Content(
                        data
                    )
                )
            }
        }
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val TAG = "process"
        private const val TEXT = "text"
        private const val PAGE = "page"
        private const val PAGE_SIZE = "20"
        private const val PER_PAGE = "per_page"
    }
}
