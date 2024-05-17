package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.network.requests.DetailsRequest
import ru.practicum.android.diploma.data.network.requests.IndustryRequest
import ru.practicum.android.diploma.data.network.requests.SearchRequest
import ru.practicum.android.diploma.data.network.responses.IndustryListDTO
import ru.practicum.android.diploma.data.network.responses.IndustryResponse
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApi
) : NetworkClient {
    private var response = Response()
    override suspend fun doRequest(dto: Any): Response {
        return if (!isConnected()) {
            Response().apply { resultCode = SERVER_ERROR }
        } else {
            when (dto) {
                is SearchRequest -> getSearchResponse(dto)
                is DetailsRequest -> getDetailsResponse(dto)
                is IndustryRequest -> getIndustryResponse()
                else -> Response().apply { resultCode = NOT_FOUND }
            }
        }
    }

    private suspend fun getSearchResponse(dto: SearchRequest): Response {
        withContext(Dispatchers.IO) {
            try {
                response = hhApi.getSearch(dto.options).apply { resultCode = OK }
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                response = Response().apply { resultCode = NOT_FOUND }
            }
        }
        return response
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

    private suspend fun getIndustryResponse(): Response {
        withContext(Dispatchers.IO) {
            try {
                response = industryMapper(hhApi.getIndustry()).apply { resultCode = OK }
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                response = Response().apply { resultCode = NOT_FOUND }
            }
        }
        return response
    }

    private fun industryMapper(array: Array<IndustryListDTO>): IndustryResponse {
        return IndustryResponse(
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
