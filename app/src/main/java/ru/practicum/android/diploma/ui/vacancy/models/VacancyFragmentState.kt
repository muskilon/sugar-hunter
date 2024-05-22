package ru.practicum.android.diploma.ui.vacancy.models

import ru.practicum.android.diploma.domain.models.VacancyDetails

sealed interface VacancyFragmentState {
    data object Start : VacancyFragmentState

    data object Loading : VacancyFragmentState

    data class Content(
        val vacancy: VacancyDetails
    ) : VacancyFragmentState

    data object Error : VacancyFragmentState

    data object Empty : VacancyFragmentState
}
