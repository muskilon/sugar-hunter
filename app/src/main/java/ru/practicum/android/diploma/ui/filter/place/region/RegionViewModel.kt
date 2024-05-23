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
    private val regions =arrayListOf<Areas>()

    fun getAreas(country: String) {
        liveState.postValue(ChoicePlaceState.Loading)
        viewModelScope.launch {
            vacanciesInterActor.getAreaDictionary().collect { areas ->
                when (areas) {
                    is Resource.ConnectionError -> liveState.postValue(ChoicePlaceState.Error(areas.message))

                    is Resource.NotFound -> liveState.postValue(ChoicePlaceState.Empty(areas.message))

                    is Resource.Data -> {
                        if (country.isNotEmpty()){
                            regions.clear()
                            regions.addAll(areas.value.filter { it.parentId != null && it.countryName == country })
                        } else {
                            regions.clear()
                            regions.addAll(areas.value.filter { it.parentId != null })
                        }
                        liveState.postValue(ChoicePlaceState.Content(regions))
                    }
                }
            }
        }
    }
    fun search(text: String) {
        if (text.isEmpty()) {
            liveState.postValue(ChoicePlaceState.Content(regions))
        } else {
            val foundAreas = arrayListOf<Areas>()
            for (area in regions) {
                if (area.name.startsWith(text, true)) {
                    foundAreas.add(area)
                }
            }
            if(foundAreas.isEmpty()) {
                liveState.postValue(ChoicePlaceState.Empty(""))
            }
            else {
                liveState.postValue(ChoicePlaceState.Content(foundAreas))
            }
        }
    }
    fun getState(): LiveData<ChoicePlaceState> = liveState
}
