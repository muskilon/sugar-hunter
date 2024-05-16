package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.models.VacansiesResponse

interface VacanciesInterActor {
    fun searchVacancies(options: Map<String, String>): Flow<Resource<VacansiesResponse>>

    suspend fun getVacancy(id: String): Flow<Resource<VacancyDetails>>

    suspend fun getIndustries(): Flow<Resource<List<Industries>>>
}
