package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.AreasDictionary
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacanciesInterActorImpl(
    private val repository: VacanciesRepository
) : VacanciesInterActor {

    override fun searchVacancies(
        options: Map<String, String>
    ): Flow<Resource<VacanciesResponse>> = repository.searchVacancies(options)

    override suspend fun getVacancy(id: String): Flow<Resource<VacancyDetails>> = repository.getVacancyDetails(id)
    override suspend fun getIndustries(): Flow<Resource<List<Industries>>> = repository.getIndustries() //IndustryInteractorImpl
    override suspend fun getAreaDictionary(): Flow<Resource<AreasDictionary>> = repository.getAreas()
}
