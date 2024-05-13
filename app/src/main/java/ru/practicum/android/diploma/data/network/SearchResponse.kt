package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.VacanciesDTO

class SearchResponse(
    val items: List<VacanciesDTO>
) : Response()
