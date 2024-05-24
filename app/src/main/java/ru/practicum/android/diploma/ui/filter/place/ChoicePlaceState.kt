package ru.practicum.android.diploma.ui.filter.place

import ru.practicum.android.diploma.domain.models.Areas

sealed class ChoicePlaceState {
    data object Loading : ChoicePlaceState()
    data class Content(
        val countries: List<Areas>
    ) : ChoicePlaceState()

    data class Error(
        val error: String
    ) : ChoicePlaceState()

    data class Empty(
        val message: String
    ) : ChoicePlaceState()
}
