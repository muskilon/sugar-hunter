package ru.practicum.android.diploma.ui.search.models

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SearchFragmentState {
    data object Start : SearchFragmentState

    data object Loading : SearchFragmentState

    data class Content(
        val vacancy: List<Vacancy>
    ) : SearchFragmentState

    data class Error(
        val errorMessage: String
    ) : SearchFragmentState

    data class Empty(
        val message: String
    ) : SearchFragmentState
}
