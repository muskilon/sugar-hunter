package ru.practicum.android.diploma.ui.filter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.models.SavedFilters

class FilterViewModel(
    val filtersInterActor: FiltersInterActor
) : ViewModel() {

    val mutable = MutableLiveData<Int>()

    fun setFilters(filters: SavedFilters?) {
        filtersInterActor.updateFilters(filters)
    }
    fun getFilters() {
        val ttt = filtersInterActor.getFilters()
        Log.d("FILTERS", ttt.toString())
    }
}
