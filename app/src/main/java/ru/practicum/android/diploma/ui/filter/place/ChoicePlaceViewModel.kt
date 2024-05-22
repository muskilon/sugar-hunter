package ru.practicum.android.diploma.ui.filter.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Resource

class ChoicePlaceViewModel(
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    val mutable = MutableLiveData<Int>()
    private val foundAreas = mutableListOf<Areas>()

    fun getAreas() {
        viewModelScope.launch {
            vacanciesInterActor.getAreaDictionary().collect {
                when (it) {
                    is Resource.ConnectionError -> Log.d("TAG", it.message)

                    is Resource.NotFound -> Log.d("TAG", it.message)

                    is Resource.Data -> {
                        val areas = it.value
                        foundAreas.clear()
                        areas.getArea("Мос")
                        Log.d("FILTER", foundAreas.toString())
                    }
                }
            }
        }
    }

    private fun List<Areas>.getArea(name: String) {
        for (area in this) {
            if (area.name.startsWith(name, true)) {
                foundAreas.add(area)
            }
        }
    }

    companion object {
        private const val TAG = "CHOOSE_PLACE"
    }
}
