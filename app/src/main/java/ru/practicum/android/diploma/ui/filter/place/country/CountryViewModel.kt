package ru.practicum.android.diploma.ui.filter.place.country

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.ui.search.SearchViewModel

class CountryViewModel(
    private val vacanciesInterActor: VacanciesInterActor,
    private val filtersInterActor: FiltersInterActor
) : ViewModel() {
    private val liveState = MutableLiveData<CountryState>()

    fun getAreas() {
        liveState.postValue(CountryState.Loading)
        viewModelScope.launch {
            vacanciesInterActor.getAreaDictionary().collect { areas ->
                when (areas) {
                    is Resource.ConnectionError -> liveState.postValue(CountryState.Error(areas.message))

                    is Resource.NotFound -> liveState.postValue(CountryState.Empty(areas.message))

                    is Resource.Data -> {
                        val countries = ArrayList<Areas>()
                        areas.value.forEach { area ->
                            if (area.parentId == null) countries.add(area)
                        }
                        liveState.postValue(CountryState.Content(countries))
                    }
                }
            }
        }
    }
    fun getState(): LiveData<CountryState> = liveState
}
