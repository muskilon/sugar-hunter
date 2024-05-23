package ru.practicum.android.diploma.domain.models



sealed class IndustryState {

    data object Loading : IndustryState()

    data class Content(
        val industriesList: ArrayList<Industries>
    ) : IndustryState()

    data object NotFound : IndustryState()

    data object ConnectionError : IndustryState()

}
