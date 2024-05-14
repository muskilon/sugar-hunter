package ru.practicum.android.diploma.data.network.responses

import ru.practicum.android.diploma.data.dto.VacanciesDTO
import ru.practicum.android.diploma.data.network.Response

class SearchResponse(
    val items: List<VacanciesDTO>
) : Response()
