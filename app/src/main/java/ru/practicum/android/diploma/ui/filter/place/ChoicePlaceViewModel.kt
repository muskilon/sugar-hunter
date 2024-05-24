package ru.practicum.android.diploma.ui.filter.place

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.Areas

class ChoicePlaceViewModel(
    private val filtersInterActor: FiltersInterActor
) : ViewModel() {
    private val workPlace = MutableLiveData<MutableMap<String, String>>()

    fun setArea(bundle: Bundle) {
        val area = HashMap<String, String>()
        with (bundle) {
            getString(REGION_NAME)?.let {
                area[REGION_NAME] = it
            }
            getString(REGION_ID)?.let {
                area[REGION_ID] = it
            }
            getString(COUNTRY_NAME)?.let {
                area[COUNTRY_NAME] = it
            }
            getString(COUNTRY_ID)?.let {
                area[COUNTRY_ID] = it
            }
        }
        workPlace.postValue(area)
    }
    fun clearRegion() {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace.remove(REGION_NAME)
        tempWorkPlace.remove(REGION_ID)
        workPlace.postValue(tempWorkPlace)
    }

    fun clearCountry() {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace.remove(COUNTRY_NAME)
        tempWorkPlace.remove(COUNTRY_ID)
        workPlace.postValue(tempWorkPlace)
    }
    fun savePlace(): Bundle {
        var bundle = bundleOf()
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace[COUNTRY_ID]?.let { id ->
            tempWorkPlace[COUNTRY_NAME]?.let { name ->
                bundle = bundleOf(REGION_ID to id, REGION_NAME to name)
            }
        }
        tempWorkPlace[REGION_ID]?.let { id ->
            tempWorkPlace[REGION_NAME]?.let { name ->
                tempWorkPlace[COUNTRY_NAME]?.let { countryName ->
                    bundle = bundleOf(REGION_ID to id, REGION_NAME to name, COUNTRY_NAME to countryName)
                }
            }
        }
        return bundle
//        bundle.getString("area")
//        Log.d("TAG", "area=${bundle.getString("area")}, areaName=${bundle.getString("areaName")}, countryName=${bundle.getString(
//            COUNTRY_NAME)}")
    }
    fun getArea() : LiveData<MutableMap<String, String>> = workPlace

    companion object {
        private const val REGION_NAME = "regionName"
        private const val REGION_ID = "regionId"
        private const val COUNTRY_NAME = "countryName"
        private const val COUNTRY_ID = "countryId"
    }
}
