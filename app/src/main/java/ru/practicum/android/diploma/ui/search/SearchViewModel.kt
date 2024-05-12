package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState

class SearchViewModel : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchFragmentState>()
    fun observeState(): LiveData<SearchFragmentState> = stateLiveData

    val mutable = MutableLiveData<Int>()

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(
                SearchFragmentState.Loading
            )
            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                renderState(
                    SearchFragmentState.Empty("Nothing found")
                )
            }
        }
    }

    private fun processResult() {

    }

    private fun renderState(state: SearchFragmentState) {
        stateLiveData.postValue(state)
    }

}
