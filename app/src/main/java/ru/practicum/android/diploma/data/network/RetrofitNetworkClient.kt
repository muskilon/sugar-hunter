package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.DTOVacancies
import ru.practicum.android.diploma.data.dto.IndustryDTO
import ru.practicum.android.diploma.data.dto.IndustryList
import ru.practicum.android.diploma.data.network.requests.DetailsRequest
import ru.practicum.android.diploma.data.network.requests.SearchRequest
import ru.practicum.android.diploma.domain.models.Resource
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApi
) : NetworkClient {
    private var response = Response()

    override suspend fun searchResponse(request: SearchRequest): Resource<DTOVacancies> {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext hhApi.getSearch(request.options).body()?.let { Resource.Data(it) } ?: Resource.NotFound("NOT_FOUND")
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                return@withContext Resource.ConnectionError("ERROR")
            }
        }
    }

    override suspend fun getIndustry(): Resource<IndustryDTO> {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext hhApi.getIndustry().body()?.let { Resource.Data(industryMapper(it)) } ?: Resource.NotFound("NOT_FOUND")
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                return@withContext Resource.ConnectionError("ERROR")
            }
        }
    }
    override suspend fun doRequest(dto: Any): Response {
        return if (!isConnected()) {
            Response().apply { resultCode = SERVER_ERROR }
        } else {
            when (dto) {
                is DetailsRequest -> getDetailsResponse(dto)
                else -> Response().apply { resultCode = NOT_FOUND }
            }
        }
    }

    private suspend fun getDetailsResponse(dto: DetailsRequest): Response {
        withContext(Dispatchers.IO) {
            try {
                response = hhApi.getVacancy(dto.id).apply { resultCode = OK }
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                response = Response().apply { resultCode = NOT_FOUND }
            }
        }
        return response
    }

    private fun industryMapper(array: Array<IndustryList>): IndustryDTO {
        return IndustryDTO(
            container = array.asList()
        )
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    companion object {
        private const val REQUEST_ERROR_TAG = "NetworkRequestError"
        private const val OK = 200
        private const val NOT_FOUND = 400
        private const val SERVER_ERROR = 500
    }
}
