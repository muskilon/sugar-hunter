package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.Key
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState

class SearchViewModel(
    private val vacanciesInterActor: VacanciesInterActor,
    private val filtersInterActor: FiltersInterActor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchFragmentState>()
    private var currentPage = 0
    private var totalPages = 0
    private var latestSearchText = ""
    private var searchJob: Job? = null
    private var isClickAllowed = true
    private var currentVacancies = listOf<Vacancy>()

    fun observeState(): LiveData<SearchFragmentState> = stateLiveData
    fun isFiltersOn(): Boolean {
        return filtersInterActor.getFilters().filters.isNotEmpty()
    }

    fun getSearchRequest(text: String, page: String?): Map<String, String> {
        val filters = filtersInterActor.getFilters().filters
        with(filters) {
            this[Key.TEXT] = text
            this[Key.PER_PAGE] = Key.PAGE_SIZE
            this[Key.REGION_ID]?.let { this[Key.AREA] = it }
            this.remove(Key.REGION_ID)
            page?.let {
                this[Key.PAGE] = page
            }

        }
        val request = filters.filterNot { it.key.startsWith(Key.NOT_REQUEST) }.toMap()
        return request
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(Key.CLICK_DEBOUNCE_DELAY)
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
                delay(Key.SEARCH_DEBOUNCE_DELAY)
                searchVacancies(getSearchRequest(text, null))
            }
        }
    }

    fun searchVacancies(request: Map<String, String>) {
        request[Key.TEXT]?.let { text ->
            stateLiveData.postValue(SearchFragmentState.Loading)
            currentVacancies = listOf()
            latestSearchText = text
            viewModelScope.launch {
                vacanciesInterActor.searchVacancies(request).collect { result ->
                    processResult(result, true)
                }
            }
        }
    }

    fun repeatRequest(searchInput: String, isNeedAddCounter: Boolean) {
        if (isNeedAddCounter) {
            currentPage++
        } else {
            stateLiveData.postValue(SearchFragmentState.Loading)
            currentPage = 0
            latestSearchText = searchInput
        }
        viewModelScope.launch {
            vacanciesInterActor
                .searchVacancies(getSearchRequest(latestSearchText, currentPage.toString()))
                .collect { result ->
                    processResult(result, false)
                }
        }
    }

    private fun processResult(foundVacancies: Resource<VacanciesResponse>, isSearch: Boolean) {
        when (foundVacancies) {
            is Resource.ConnectionError -> {
                if (!isSearch) currentPage--
                stateLiveData.postValue(
                    SearchFragmentState.Error(
                        foundVacancies.message,
                        isSearch = isSearch
                    )
                )
            }

            is Resource.NotFound -> {
                stateLiveData.postValue(
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
                stateLiveData.postValue(
                    SearchFragmentState.Content(data)
                )
            }
        }
    }
}
