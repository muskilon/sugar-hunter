package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        var response = Response()
        if (!isConnected()) {
            response.resultCode = SERVER_ERROR
        } else if ((dto !is SearchRequest) && (dto !is DetailsRequest)) {
            response.resultCode = NOT_FOUND
        } else {
            when (dto) {
                is SearchRequest -> {
                    response = withContext(Dispatchers.IO) {
                        try {
                            val apiResponse = hhApi.getSearch(dto.text)
                            apiResponse.apply { resultCode = OK }
                        } catch (ex: IOException) {
                            Log.e(REQUEST_ERROR_TAG, ex.toString())
                            Response().apply { resultCode = NOT_FOUND }
                        }
                    }
                }
                is DetailsRequest ->{
                    response = withContext(Dispatchers.IO) {
                        try {
                            val apiResponse = hhApi.getVacancy(dto.id)
                            apiResponse.apply { resultCode = OK }
                        } catch (ex: IOException) {
                            Log.e(REQUEST_ERROR_TAG, ex.toString())
                            Response().apply { resultCode = NOT_FOUND }
                        }
                    }
                }
            }
        }
        return response
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
