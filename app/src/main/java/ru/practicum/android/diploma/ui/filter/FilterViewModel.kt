package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.models.SavedFilters
import ru.practicum.android.diploma.ui.Key

class FilterViewModel(
    val filtersInterActor: FiltersInterActor
) : ViewModel() {
    private val filters = MutableLiveData<MutableMap<String, String>>()

    fun getFilters(): LiveData<MutableMap<String, String>> = filters
    fun getFiltersFromStorage(): MutableMap<String, String> {
        val newFilters = filtersInterActor.getFilters().filters
        Log.d("NEW_FILTERS_TAG", newFilters.toString())
        filters.value = newFilters
        Log.d("TEMP_LIVE_FILTERS_TAG", filters.value.toString())
        return newFilters
    }

    fun updateFiltersInStorage() {
        val tempFilters = getTempFilters()
        filtersInterActor.updateFilters(SavedFilters(tempFilters))
//        Log.d("TEMP_FILTERS_TAG", tempFilters.toString())
//        Log.d("TAG_SHARED AFTER", filtersInterActor.getFilters().filters.toString())
    }

    fun setSalary(salary: CharSequence?) {
        val tempFilters = getTempFilters()
        if (salary.isNullOrEmpty()) {
            tempFilters.remove(Key.SALARY)
        } else {
            tempFilters[Key.SALARY] = salary.toString()
        }
        filters.value = tempFilters
        Log.d("SET_SALARY_TAG", filters.value.toString())

    }

    fun salaryCheckBoxProcessing(isChecked: Boolean) {
        val tempFilters = getTempFilters()
        if (isChecked) {
            tempFilters[Key.ONLY_WITH_SALARY] = Key.TRUE
        } else {
            tempFilters.remove(Key.ONLY_WITH_SALARY)
        }
        filters.value = tempFilters
    }

    fun clearRegion() {
        val tempFilters = getTempFilters()
        tempFilters.remove(Key.REGION_NAME)
        tempFilters.remove(Key.REGION_ID)
        tempFilters.remove(Key.COUNTRY_NAME)
        tempFilters.remove(Key.COUNTRY_ID)
        filters.value = tempFilters
    }

    fun clearFilters() {
        filters.value = mutableMapOf()
    }

    fun getBundle(): Bundle {
        val tempFilters = getTempFilters()
        return bundleOf(
            Key.REGION_NAME to tempFilters[Key.REGION_NAME],
            Key.REGION_ID to tempFilters[Key.REGION_ID],
            Key.COUNTRY_NAME to tempFilters[Key.COUNTRY_NAME],
            Key.COUNTRY_ID to tempFilters[Key.COUNTRY_ID]
        )
    }

    fun processBundle(bundle: Bundle) {
        val tempFilters = getTempFilters()
        if (!bundle.isEmpty) {
            with(bundle) {
                getString(Key.REGION_NAME)?.let {
                    tempFilters[Key.REGION_NAME] = it
                }
                getString(Key.REGION_ID)?.let {
                    tempFilters[Key.REGION_ID] = it
                }
                getString(Key.COUNTRY_NAME)?.let {
                    tempFilters[Key.COUNTRY_NAME] = it
                }
                getString(Key.COUNTRY_ID)?.let {
                    tempFilters[Key.COUNTRY_ID] = it
                }
            }
            filters.value = tempFilters
        }
    }

    private fun getTempFilters(): MutableMap<String, String> {
        val tempFilters: MutableMap<String, String> = mutableMapOf()
        filters.value?.let { tempFilters.putAll(it) }
        return tempFilters
    }
}
