package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.DTOVacancies
import ru.practicum.android.diploma.data.dto.IndustryDTO
import ru.practicum.android.diploma.data.network.requests.SearchRequest
import ru.practicum.android.diploma.domain.models.Resource

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

    suspend fun searchResponse(request: SearchRequest): Resource<DTOVacancies>

    suspend fun getIndustry(): Resource<IndustryDTO>
}
