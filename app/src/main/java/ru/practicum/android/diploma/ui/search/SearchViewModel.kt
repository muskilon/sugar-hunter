package ru.practicum.android.diploma.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.AreaItem
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState

class SearchViewModel(
    private val vacanciesInterActor: VacanciesInterActor,
    private val filtersInterActor: FiltersInterActor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchFragmentState>()
    private val isLoading = MutableLiveData<Boolean>()
    private val foundAreas = mutableListOf<AreaItem>() // Для тестирования
    private var currentPage = 0
    private var totalPages = 0
    private var latestSearchText = ""
    private var searchJob: Job? = null
    private var isClickAllowed = true
    private var currentVacancies = listOf<Vacancy>()

    fun observeState(): LiveData<SearchFragmentState> = stateLiveData
    fun observeIsLoading(): LiveData<Boolean> = isLoading

    fun isFiltersOn() : Boolean{
        return filtersInterActor.getFilters() != null
    }

    fun getSearchRequest(text: String, page: String?): HashMap<String, String> {
        val filter = filtersInterActor.getFilters()
        val request: HashMap<String, String> = HashMap()
        with(request) {
            this[TEXT] = text
            this[PER_PAGE] = PAGE_SIZE
            if (!page.isNullOrEmpty()) {
                this[PAGE] = page
            }
            filter?.let {
                if (!it.areaId.isNullOrEmpty()) {
                    this[AREA] = it.areaId
                }
                if (!it.industryId.isNullOrEmpty()) {
                    this[INDUSTRY] = it.industryId
                }
                if (!it.salary.isNullOrEmpty()) {
                    this[SALARY] = it.salary
                }
                if (it.onlyWithSalary) {
                    this[ONLY_WITH_SALARY] = it.onlyWithSalary.toString()
                }
            }
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
        request[TEXT]?.let { text ->
            if (latestSearchText == text || text.isEmpty()) {
                searchJob?.cancel()
            } else {
                stateLiveData.postValue(SearchFragmentState.Loading)
                currentVacancies = listOf()
                latestSearchText = text
                viewModelScope.launch {
                    vacanciesInterActor
                        .searchVacancies(request)
                        .collect { result ->
                            processResult(result, true)
                        }
                }
            }
        }
    }

    fun onLastItemReached() {
        currentPage++
        isLoading.postValue(true)
        viewModelScope.launch {
            vacanciesInterActor.searchVacancies(getSearchRequest(latestSearchText, currentPage.toString()))
                .collect { result ->
                    processResult(result, false)
                }
        }
    }

// ДЛЯ ТЕСТИРОВАНИЯ!!!

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

// ДЛЯ ТЕСТИРОВАНИЯ!!!

    private fun processResult(foundVacancies: Resource<VacanciesResponse>, isSearch: Boolean) {
        when (foundVacancies) {
            is Resource.ConnectionError -> {
                stateLiveData.postValue(
                    SearchFragmentState.Error(
                        foundVacancies.message,
                        isSearch
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
                    SearchFragmentState.Content(
                        data
                    )
                )
                isLoading.postValue(false)
            }
        }
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val TAG = "process"

//        API параметры

        private const val TEXT = "text"
        private const val PAGE = "page"
        private const val PAGE_SIZE = "20"
        private const val PER_PAGE = "per_page"
        private const val AREA = "area"
        private const val INDUSTRY = "industry"
        private const val SALARY = "salary"
        private const val ONLY_WITH_SALARY = "only_with_salary"
    }
}
