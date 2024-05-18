package ru.practicum.android.diploma.ui.filter.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.dto.AreaItemDTO
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.AreaItem
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.ui.search.SearchViewModel

class ChoicePlaceViewModel(
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    val mutable = MutableLiveData<Int>()
    private val foundAreas = mutableListOf<AreaItem>()

    fun getAreas(){
        viewModelScope.launch {
            vacanciesInterActor.getAreaDictionary().collect {
                when (it) {
                    is Resource.ConnectionError -> Log.d("FILTER", it.message)

                    is Resource.NotFound -> Log.d("FILTER", it.message)

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
            if (area.name.startsWith(name, true))
                foundAreas.add(area)
            val found = area.areas?.getArea(name)
            if (found != null)
                return found
        }
        return null
    }
}
