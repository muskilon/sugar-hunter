package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.network.Response

class SearchResponseDTO(
    val items: List<VacanciesDTO>,
    val pages: Int,
    val page: Int,
    val found: Int
) : Response()
