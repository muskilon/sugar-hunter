package ru.practicum.android.diploma.ui.filter.place

import android.os.Bundle
import android.util.Log
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
        Log.d("SET_AREA_TAG", area.toString())
        workPlace.postValue(area)
    }

    fun clearRegion(isCountryVisible: Boolean) {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        if (isCountryVisible) {
            tempWorkPlace[Key.COUNTRY_NAME]?.let { tempWorkPlace[Key.REGION_NAME] = it }
            tempWorkPlace[Key.COUNTRY_ID]?.let { tempWorkPlace[Key.REGION_ID] = it }
        } else {
            tempWorkPlace.clear()
        }
        Log.d("CLEAR_REGION_TAG", tempWorkPlace.toString())
        workPlace.postValue(tempWorkPlace)
    }

    fun clearCountry() {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace.clear() // TODO почистить
        workPlace.postValue(mutableMapOf())
    }

    fun savePlace(): Bundle {
        val bundle = bundleOf()
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace.forEach{
            bundle.putString(it.key, it.value)
        }
        Log.d("BUNDLE_TAG", "${ bundle.getString(Key.REGION_ID, null) }, ${ bundle.getString(Key.REGION_NAME, null) }, ${ bundle.getString(Key.COUNTRY_ID, null)}, ${ bundle.getString(Key.COUNTRY_NAME, null) }")
        return bundle
    }

    fun getArea(): LiveData<MutableMap<String, String>> = workPlace
}
