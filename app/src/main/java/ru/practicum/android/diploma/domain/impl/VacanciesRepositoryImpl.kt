package ru.practicum.android.diploma.domain.impl

import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.network.DetailsRequest
import ru.practicum.android.diploma.data.network.DetailsResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.SearchRequest
import ru.practicum.android.diploma.data.network.SearchResponse
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
) : VacanciesRepository {
    override fun searchVacancies(
        text: String
    ): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(SearchRequest(text))
        when (response.resultCode) {
            OK -> {
                with(response as SearchResponse) {
                    val data = items.map {
                        val vacancy = Vacancy(
                            id = it.id,
                            title = it.name,
                            city = it.area.name,
                            employer = it.employer.name,
                            logos = it.employer.logoUrls,
                            salary = it.salary,
                            vacancyUrl = it.url.toUri()
                        )
                        vacancy
                    }
                    if (data.isEmpty()) {
                        emit(Resource.NotFound("not_found"))
                    } else {
                        emit(Resource.Data(data))
                    }
                }
            }
            in NOT_FOUND -> emit(Resource.NotFound("not_found"))
            else -> {
                emit(Resource.ConnectionError("connection_error"))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getVacancy(
        id: String
    ) : Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.doRequest(DetailsRequest(id))
        when (response.resultCode) {
            OK -> {
                with(response as DetailsResponse) {
                    val data = VacancyDetails(
                        id = this.id,
                        title = this.name
                    )
                    data
                }
            }
            in NOT_FOUND -> emit(Resource.NotFound("not_found"))
            else -> {
                emit(Resource.ConnectionError("connection_error"))
            }
        }
    }

    companion object {
        private const val OK = 200
        private val NOT_FOUND = listOf(400, 404)
    }
}
