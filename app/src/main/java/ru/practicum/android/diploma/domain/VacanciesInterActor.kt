package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface VacanciesInterActor {
    fun searchVacancies(options: Map<String, String>): Flow<Resource<VacanciesResponse>>

    suspend fun getVacancy(id: String): Flow<Resource<VacancyDetails>>

    suspend fun getIndustries(): Flow<Resource<List<Industry>>>

    suspend fun getAreaDictionary(): Flow<Resource<List<Areas>>>
}
