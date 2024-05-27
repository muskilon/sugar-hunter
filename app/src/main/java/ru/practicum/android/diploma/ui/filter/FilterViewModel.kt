package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
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
        filters.postValue(newFilters)
        return newFilters
    }

    fun updateFiltersInStorage() {
        val tempFilters: MutableMap<String, String> = mutableMapOf()
        filters.value?.let { tempFilters.putAll(it) }
        filtersInterActor.updateFilters(SavedFilters(tempFilters))
    }

    fun setSalary(salary: CharSequence?) {
        val tempFilters: MutableMap<String, String> = mutableMapOf()
        filters.value?.let { tempFilters.putAll(it) }
        salary?.let { tempFilters[Key.SALARY] = it.toString() } ?: tempFilters.remove(Key.SALARY)
        filters.postValue(tempFilters)
    }

    fun salaryCheckBoxProcessing(isChecked: Boolean) {
        val tempFilters: MutableMap<String, String> = mutableMapOf()
        filters.value?.let { tempFilters.putAll(it) }
        if (isChecked) {
            tempFilters[Key.ONLY_WITH_SALARY] = Key.TRUE
        } else {
            tempFilters.remove(Key.ONLY_WITH_SALARY)
        }
        filters.postValue(tempFilters)
    }

    fun clearRegion() {
        val tempFilters: MutableMap<String, String> = mutableMapOf()
        filters.value?.let { tempFilters.putAll(it) }
        tempFilters.remove(Key.REGION_NAME)
        tempFilters.remove(Key.REGION_ID)
        tempFilters.remove(Key.COUNTRY_NAME)
        tempFilters.remove(Key.COUNTRY_ID)
        filters.postValue(tempFilters)
    }

    fun clearFilters() {
        filters.postValue(mutableMapOf())
    }

    fun getBundle(): Bundle {
        val tempFilters: MutableMap<String, String> = mutableMapOf()
        filters.value?.let { tempFilters.putAll(it) }
        return bundleOf(
            Key.REGION_NAME to tempFilters[Key.REGION_NAME],
            Key.REGION_ID to tempFilters[Key.REGION_ID],
            Key.COUNTRY_NAME to tempFilters[Key.COUNTRY_NAME],
            Key.COUNTRY_ID to tempFilters[Key.COUNTRY_ID]
        )
    }

    fun processBundle(bundle: Bundle) {
        val tempFilters: MutableMap<String, String> = mutableMapOf()
        filters.value?.let { tempFilters.putAll(it) }
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
            filters.postValue(tempFilters)
        }
    }
}
