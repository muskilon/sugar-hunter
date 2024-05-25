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

    fun setAreaFromFilters(bundle: Bundle) {
        val area = HashMap<String, String>()
        with(bundle) {
            if (getString(Key.COUNTRY_NAME).isNullOrEmpty()) {
                getString(Key.REGION_NAME)?.let {
                    area[Key.COUNTRY_NAME] = it
                }
                getString(Key.REGION_ID)?.let {
                    area[Key.REGION_ID] = it
                }
            } else {
                getString(Key.REGION_NAME)?.let {
                    area[Key.REGION_NAME] = it
                }
                getString(Key.REGION_ID)?.let {
                    area[Key.REGION_ID] = it
                }
                getString(Key.COUNTRY_NAME)?.let {
                    area[Key.COUNTRY_NAME] = it
                }
            }
        }
        workPlace.postValue(area)
    }

    fun clearRegion() {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace.remove(Key.REGION_NAME)
        tempWorkPlace.remove(Key.REGION_ID)
        workPlace.postValue(tempWorkPlace)
    }

    fun clearCountry() {
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace.remove(Key.COUNTRY_NAME)
        tempWorkPlace.remove(Key.COUNTRY_ID)
        workPlace.postValue(tempWorkPlace)
    }

    fun savePlace(): Bundle {
        var bundle = bundleOf()
        val tempWorkPlace: MutableMap<String, String> = mutableMapOf()
        workPlace.value?.let { tempWorkPlace.putAll(it) }
        tempWorkPlace[Key.COUNTRY_ID]?.let { id ->
            tempWorkPlace[Key.COUNTRY_NAME]?.let { name ->
                bundle = bundleOf(
                    Key.REGION_ID to id, Key.REGION_NAME to name
                )
            }
        }
        tempWorkPlace[Key.REGION_ID]?.let { id ->
            tempWorkPlace[Key.REGION_NAME]?.let { name ->
                tempWorkPlace[Key.COUNTRY_NAME]?.let { countryName ->
                    bundle = bundleOf(
                        Key.REGION_ID to id, Key.REGION_NAME to name, Key.COUNTRY_NAME to countryName
                    )
                }
            }
        }
        return bundle
    }

    fun getArea(): LiveData<MutableMap<String, String>> = workPlace
}
