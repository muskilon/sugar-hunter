package ru.practicum.android.diploma.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewModel(
    private val vacanciesInterActor: VacanciesInterActor
) : ViewModel() {

    val mutable = MutableLiveData<Int>()

    fun searchVacancies(text: String) {
        viewModelScope.launch {
            vacanciesInterActor.searchVacancies(text).collect { result ->
                processResult(result)
            }
        }
    }

    private fun processResult(foundVacancies: Resource<List<Vacancy>>) {
        when (foundVacancies) {
            is Resource.ConnectionError -> Log.d(TAG, foundVacancies.message)

            is Resource.NotFound -> Log.d(TAG, foundVacancies.message)

            is Resource.Data -> Log.d(TAG, foundVacancies.value.toString())
        }
    }

    companion object {
        private const val TAG = "process"
    }
}
