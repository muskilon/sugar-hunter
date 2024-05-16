package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.VacanciesDTO
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.requests.DetailsRequest
import ru.practicum.android.diploma.data.network.requests.IndustryRequest
import ru.practicum.android.diploma.data.network.requests.SearchRequest
import ru.practicum.android.diploma.data.network.responses.DetailsResponse
import ru.practicum.android.diploma.data.network.responses.IndustryResponse
import ru.practicum.android.diploma.data.network.responses.KeySkills
import ru.practicum.android.diploma.data.network.responses.SearchResponse
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.PageInfo
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.domain.models.VacansiesResponse

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
) : VacanciesRepository {

    override fun searchVacancies(
        options: Map<String, String>
    ): Flow<Resource<VacansiesResponse>> = flow {
        val response = networkClient.doRequest(SearchRequest(options))
        when (response.resultCode) {
            OK -> {
                with(response as SearchResponse) {
                        val vacancyResponse = VacansiesResponse(
                            page = page,
                            pages = pages,
                            found = found,
                            items = items.map {
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
                        )

                    if (vacancyResponse.items.isEmpty()) {
                        emit(Resource.NotFound(NOT_FOUND_TEXT))
                    } else {
                        emit(Resource.Data(vacancyResponse))
                    }
                }
            }

            in NOT_FOUND -> emit(Resource.NotFound(NOT_FOUND_TEXT))
            else -> {
                emit(Resource.ConnectionError(CONNECTION_ERROR))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getVacancy(
        id: String
    ): Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.doRequest(DetailsRequest(id))
        when (response.resultCode) {
            OK -> {
                with(response as DetailsResponse) {
                    val data = VacancyDetails(
                        id = this.id,
                        title = this.name,
                        area = this.area,
                        employer = this.employer,
                        salary = this.salary,
                        experience = this.experience,
                        employment = this.employment,
                        schedule = this.schedule,
                        description = this.description,
                        keySkills = skillsMapper(this.keySkills),
                        contacts = this.contacts
                    )
                    emit(Resource.Data(data))
                }
            }

            in NOT_FOUND -> emit(Resource.NotFound(NOT_FOUND_TEXT))
            else -> {
                emit(Resource.ConnectionError(CONNECTION_ERROR))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getIndustries(): Flow<Resource<List<Industries>>> = flow {
        val response = networkClient.doRequest(IndustryRequest())
        when (response.resultCode) {
            OK -> {
                with(response as IndustryResponse) {
                    val data = container.map {
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

            in NOT_FOUND -> emit(Resource.NotFound(NOT_FOUND_TEXT))
            else -> {
                emit(Resource.ConnectionError(CONNECTION_ERROR))
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
        private const val OK = 200
        private val NOT_FOUND = listOf(400, 404)
        private const val CONNECTION_ERROR = "connection_error"
        private const val NOT_FOUND_TEXT = "not_found"
    }
}
