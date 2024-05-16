package ru.practicum.android.diploma.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.KeySkills
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
) : VacanciesRepository {

    override fun searchVacancies(
        options: Map<String, String>
    ): Flow<Resource<List<Vacancy>>> = flow {
        when (val response = networkClient.getVacancies(options)) {
            is Resource.Data -> {
                with(response) {
                    val data = this.value.items.map {
                        val vacancy = Vacancy(
                            id = it.id,
                            title = it.name,
                            city = it.area.name,
                            employer = it.employer.name,
                            logos = it.employer.logoUrls,
                            salary = it.salary
                        )
                        vacancy
                    }
                    if (data.isEmpty()) {
                        emit(Resource.NotFound(EMPTY))
                    } else {
                        emit(Resource.Data(data))
                    }
                }
            }
            is Resource.NotFound -> emit(Resource.NotFound(response.message))
            is Resource.ConnectionError -> {
                emit(Resource.ConnectionError(response.message))
            }
        }
    }.flowOn(Dispatchers.IO)
    override suspend fun getIndustries(): Flow<Resource<List<Industries>>> = flow {
        when (val response = networkClient.getIndustry()) {
            is Resource.Data -> {
                with(response) {
                    val data = this.value.container.map {
                        val industries = Industries(
                            id = it.id,
                            name = it.name,
                            industries = it.industries
                        )
                        industries
                    }
                    emit(Resource.Data(data))
                }
            }

            is Resource.NotFound -> emit(Resource.NotFound(response.message))
            is Resource.ConnectionError -> {
                emit(Resource.ConnectionError(response.message))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getVacancyDetails(
        id: String
    ): Flow<Resource<VacancyDetails>> = flow {
        when (val response = networkClient.getVacancyDetails(id)) {
            is Resource.Data -> {
                with(response) {
                    val data = VacancyDetails(
                        id = this.value.id,
                        title = this.value.name,
                        area = this.value.area,
                        employer = this.value.employer,
                        salary = this.value.salary,
                        experience = this.value.experience,
                        employment = this.value.employment,
                        schedule = this.value.schedule,
                        description = this.value.description,
                        keySkills = skillsMapper(this.value.keySkills),
                        contacts = this.value.contacts
                    )
                    emit(Resource.Data(data))
                }
            }
            is Resource.NotFound -> emit(Resource.NotFound(response.message))
            is Resource.ConnectionError -> {
                emit(Resource.ConnectionError(response.message))
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun skillsMapper(list: List<KeySkills>?): List<String>? {
        if (list.isNullOrEmpty()) {
            return null
        }
        return list.mapNotNull { it.name }
    }

    companion object {
        private const val EMPTY = "EMPTY"
    }
}
