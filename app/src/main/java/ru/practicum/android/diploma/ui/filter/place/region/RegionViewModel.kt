package ru.practicum.android.diploma.ui.filter.place.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceState

class RegionViewModel(
                      private val vacanciesInterActor: VacanciesInterActor,
                      private val filtersInterActor: FiltersInterActor
) : ViewModel() {
    private val liveState = MutableLiveData<ChoicePlaceState>()

    fun getAreas() {
        liveState.postValue(ChoicePlaceState.Loading)
        viewModelScope.launch {
            vacanciesInterActor.getAreaDictionary().collect { areas ->
                when (areas) {
                    is Resource.ConnectionError -> liveState.postValue(ChoicePlaceState.Error(areas.message))

                    is Resource.NotFound -> liveState.postValue(ChoicePlaceState.Empty(areas.message))

                    is Resource.Data -> {
                        val countries = ArrayList<Areas>()
                        areas.value.forEach { area ->
                            if (area.parentId != null) countries.add(area)
                        }
                        liveState.postValue(ChoicePlaceState.Content(countries))
                    }
                }
            }
        }
    }
    fun getState(): LiveData<ChoicePlaceState> = liveState
}
