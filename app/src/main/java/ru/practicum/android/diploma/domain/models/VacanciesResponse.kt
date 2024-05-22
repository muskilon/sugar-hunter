package ru.practicum.android.diploma.domain.models

data class VacanciesResponse(
    val items: List<Vacancy>,
    val pages: Int,
    val page: Int,
    val found: Int
)
