package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.network.responses.DetailsResponse
import ru.practicum.android.diploma.data.network.responses.IndustryResponse
import ru.practicum.android.diploma.data.network.responses.SearchResponse
import ru.practicum.android.diploma.domain.models.Resource

interface NetworkClient {
    suspend fun getVacancies(request: Map<String, String>): Resource<SearchResponse>

    suspend fun getIndustry(): Resource<IndustryResponse>

    suspend fun getVacancyDetails(id: String): Resource<DetailsResponse>
}
