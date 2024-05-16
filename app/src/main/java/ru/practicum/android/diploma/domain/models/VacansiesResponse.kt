package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.VacanciesDTO

data class VacansiesResponse(
    val items: List<Vacancy>,
    val pages: Int,
    val page: Int,
    val found: Int
)
