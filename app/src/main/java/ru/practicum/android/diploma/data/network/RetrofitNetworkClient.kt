package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response { //placeholder
        return try {
            if (!isConnected()) {
                return Response().apply { resultCode = SERVER_ERROR }
            }
            if (dto is SearchRequest) {
                withContext(Dispatchers.IO) {
                    val response = hhApi.getSearch(dto.query) //  placeholder
                    response.apply { resultCode = OK }
                }
            } else {
                Response().apply { resultCode = NOT_FOUND }
            }
        } catch (ex: AccessDeniedException) {
            Response().apply { resultCode = NOT_FOUND }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
    companion object {
        private const val OK = 200
        private const val NOT_FOUND = 400
        private const val SERVER_ERROR = 500
    }

}
