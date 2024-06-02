package ru.practicum.android.diploma.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.DTOToDataMappers
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.IndustryRepository
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient,
    private val mapper: DTOToDataMappers
) : IndustryRepository {
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

}
