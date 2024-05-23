package ru.practicum.android.diploma.ui.filter.place

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.Areas

class ChoicePlaceViewModel(
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    private val workPlace = MutableLiveData<MutableMap<String, String>>()
    private val foundAreas = mutableListOf<Areas>()
//    private val workPlace = HashMap<String, String>()

    fun setArea(bundle: Bundle) {
        val area = HashMap<String, String>()
        with (bundle) {
            getString("regionName")?.let {
                area["regionName"] = it
            }
            getString("regionId")?.let {
                area["regionId"] = it
            }
            getString("countryName")?.let {
                area["countryName"] = it
            }
            getString("countryId")?.let {
                area["countryId"] = it
            }
        }
        workPlace.postValue(area)
    }
    fun clearRegion() {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
            tempWorkPlace.remove("regionName")
            tempWorkPlace.remove("regionId")
        workPlace.postValue(tempWorkPlace)
    }

    fun clearCountry() {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace.remove("countryName")
        tempWorkPlace.remove("countryId")
        workPlace.postValue(tempWorkPlace)
    }
    fun getArea() : LiveData<MutableMap<String, String>> = workPlace

//    fun getAreas() {
//        viewModelScope.launch {
//            vacanciesInterActor.getAreaDictionary().collect {
//                when (it) {
//                    is Resource.ConnectionError -> Log.d("TAG", it.message)
//
//                    is Resource.NotFound -> Log.d("TAG", it.message)
//
//                    is Resource.Data -> {
//                        val areas = it.value
//                        foundAreas.clear()
//                        areas.getArea("Мос")
//                        Log.d("FILTER", foundAreas.toString())
//                    }
//                }
//            }
//        }
//    }
//
//    private fun List<Areas>.getArea(name: String) {
//        for (area in this) {
//            if (area.name.startsWith(name, true)) {
//                foundAreas.add(area)
//            }
//        }
//    }

    companion object {
        private const val TAG = "CHOOSE_PLACE"
    }
}
