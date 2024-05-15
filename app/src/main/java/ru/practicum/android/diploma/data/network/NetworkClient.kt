package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.DetailsVacancyDTO
import ru.practicum.android.diploma.data.dto.IndustryDTO
import ru.practicum.android.diploma.data.dto.VacanciesDTO
import ru.practicum.android.diploma.domain.models.Resource

interface NetworkClient {
    suspend fun getVacancies(request: Map<String, String>): Resource<VacanciesDTO>

    suspend fun getIndustry(): Resource<IndustryDTO>

    suspend fun getVacancyDetails(id: String): Resource<DetailsVacancyDTO>
}
