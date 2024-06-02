package ru.practicum.android.diploma.ui.filter.place

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.ui.Key

class ChoicePlaceViewModel : ViewModel() {
    private val workPlace = MutableLiveData<MutableMap<String, String>>()

    fun setArea(bundle: Bundle) {
        val area = HashMap<String, String>()
        with(bundle) {
            getString(Key.REGION_NAME)?.let {
                area[Key.REGION_NAME] = it
            }
            getString(Key.REGION_ID)?.let {
                area[Key.REGION_ID] = it
            }
            getString(Key.COUNTRY_NAME)?.let {
                area[Key.COUNTRY_NAME] = it
            }
            getString(Key.COUNTRY_ID)?.let {
                area[Key.COUNTRY_ID] = it
            }
        }
        workPlace.postValue(area)
    }

    fun clearRegion() {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace[Key.COUNTRY_NAME]?.let { tempWorkPlace[Key.REGION_NAME] = it }
        tempWorkPlace[Key.COUNTRY_ID]?.let { tempWorkPlace[Key.REGION_ID] = it }

        workPlace.postValue(tempWorkPlace)
    }

    fun clearCountry() {
        workPlace.postValue(mutableMapOf())
    }

    fun savePlace(): Bundle {
        val bundle = bundleOf()
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace.forEach {
            bundle.putString(it.key, it.value)
        }
        return bundle
    }

    fun getArea(): LiveData<MutableMap<String, String>> = workPlace
}
