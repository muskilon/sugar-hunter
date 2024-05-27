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
        filters.value = newFilters
        return newFilters
    }

    fun updateFiltersInStorage() {
        val tempFilters = getTempFilters()
        filtersInterActor.updateFilters(SavedFilters(tempFilters))
    }

    fun setSalary(salary: CharSequence?) {
        val tempFilters = getTempFilters()
        if (salary.isNullOrEmpty()) {
            tempFilters.remove(Key.SALARY)
        } else {
            tempFilters[Key.SALARY] = salary.toString()
        }
        filters.value = tempFilters

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

    fun clearIndustry() {
        val tempFilters = getTempFilters()
        tempFilters.remove(Key.INDUSTRY)
        tempFilters.remove(Key.INDUSTRY_NAME)
        filters.value = tempFilters
    }

    fun clearFilters() {
        filters.value = mutableMapOf()
    }

    fun getAreaBundle(): Bundle {
        val tempFilters = getTempFilters()
        return bundleOf(
            Key.REGION_NAME to tempFilters[Key.REGION_NAME],
            Key.REGION_ID to tempFilters[Key.REGION_ID],
            Key.COUNTRY_NAME to tempFilters[Key.COUNTRY_NAME],
            Key.COUNTRY_ID to tempFilters[Key.COUNTRY_ID]
        )
    }

    fun getIndustryBundle(): Bundle {
        val tempFilters = getTempFilters()
        return bundleOf(
            Key.INDUSTRY_NAME to tempFilters[Key.INDUSTRY_NAME],
            Key.INDUSTRY to tempFilters[Key.INDUSTRY]
        )
    }

    fun processIndustryBundle(bundle: Bundle) {
        val tempFilters = getTempFilters()
        if (!bundle.isEmpty) {
            with(bundle) {
                getString(Key.INDUSTRY)?.let {
                    tempFilters[Key.INDUSTRY] = it
                }
                getString(Key.INDUSTRY_NAME)?.let {
                    tempFilters[Key.INDUSTRY_NAME] = it
                }
            }
            filters.value = tempFilters
        }
    }

    fun processAreaBundle(bundle: Bundle) {
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
