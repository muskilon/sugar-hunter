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
import ru.practicum.android.diploma.data.network.responses.IndustryList
import ru.practicum.android.diploma.data.network.responses.IndustryResponse
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return if (!isConnected()) {
            Response().apply { resultCode = SERVER_ERROR }
        } else {
            withContext(Dispatchers.IO) {
                try {
                    when (dto) {
                        is SearchRequest -> hhApi.getSearch(dto.text).apply { resultCode = OK }
                        is DetailsRequest -> hhApi.getVacancy(dto.id).apply { resultCode = OK }
                        is IndustryRequest -> mapper(hhApi.getIndustry()).apply { resultCode = OK }
                        else -> Response().apply { resultCode = NOT_FOUND }
                    }
                } catch (ex: IOException) {
                    Log.e(REQUEST_ERROR_TAG, ex.toString())
                    Response().apply { resultCode = NOT_FOUND }
                }
            }
        }
    }

    private fun mapper(array: Array<IndustryList>): IndustryResponse {
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
