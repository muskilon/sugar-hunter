package ru.practicum.android.diploma.ui.filter.place.country

import ru.practicum.android.diploma.domain.models.Areas

sealed class CountryState {
    data object Loading : CountryState()
    data class Content(
        val countries: List<Areas>
    ) : CountryState()

    data class Error(
        val error: String
    ) : CountryState()

    data class Empty(
        val message: String
    ) : CountryState()
}
