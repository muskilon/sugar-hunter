package ru.practicum.android.diploma.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.DTOToDataMappers
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.domain.models.VacancyDetails

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: DTOToDataMappers
) : VacanciesRepository {

    override fun searchVacancies(
        options: Map<String, String>
    ): Flow<Resource<VacanciesResponse>> = flow {
        when (val response = networkClient.getVacancies(options)) {
            is Resource.Data -> {
                with(response) {
                    val data = mapper.mapSearchResponseToVacanciesResponse(this.value)
                    if (data.items.isEmpty()) {
                        emit(Resource.NotFound(String()))
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

    override suspend fun getVacancyDetails(
        id: String
    ): Flow<Resource<VacancyDetails>> = flow {
        when (val response = networkClient.getVacancyDetails(id)) {
            is Resource.Data -> {
                with(response) {
                    val data = mapper.mapDetailsResponseToVacancyDetails(this.value)
                    emit(Resource.Data(data))
                }
            }
            is Resource.NotFound -> emit(Resource.NotFound(response.message))
            is Resource.ConnectionError -> {
                emit(Resource.ConnectionError(response.message))
            }
        }
    }.flowOn(Dispatchers.IO) // перенесла

    override suspend fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        when (val response = networkClient.getIndustry()) {
            is Resource.Data -> {
                with(response) {
                    val data = mapper.industryResponseToIndustries(this.value)
                    emit(Resource.Data(data))
                }
            }
            is Resource.NotFound -> emit(Resource.NotFound(response.message))
            is Resource.ConnectionError -> {
                emit(Resource.ConnectionError(response.message))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAreas(): Flow<Resource<List<Areas>>> = flow {
        when (val response = networkClient.getAreasDictionary()) {
            is Resource.Data -> {
                with(response) {
                    val data = mapper.areasDictionaryToList(this.value.container)
                    emit(Resource.Data(data))
                }
            }
            is Resource.NotFound -> emit(Resource.NotFound(response.message))
            is Resource.ConnectionError -> {
                emit(Resource.ConnectionError(response.message))
            }
        }
    }.flowOn(Dispatchers.IO)
}
