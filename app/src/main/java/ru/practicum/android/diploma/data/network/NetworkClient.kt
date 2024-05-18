package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.AreasDictionaryDTO
import ru.practicum.android.diploma.data.dto.DetailsResponse
import ru.practicum.android.diploma.data.dto.IndustryResponse
import ru.practicum.android.diploma.data.dto.SearchResponseDTO
import ru.practicum.android.diploma.domain.models.Resource

interface NetworkClient {
    suspend fun getVacancies(request: Map<String, String>): Resource<SearchResponseDTO>

    suspend fun getIndustry(): Resource<IndustryResponse>

    suspend fun getVacancyDetails(id: String): Resource<DetailsResponse>

    suspend fun getAreasDictionary(): Resource<AreasDictionaryDTO>
}
