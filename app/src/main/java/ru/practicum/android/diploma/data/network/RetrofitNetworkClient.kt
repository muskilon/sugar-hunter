package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.VacanciesDTO
import ru.practicum.android.diploma.data.dto.DetailsVacancyDTO
import ru.practicum.android.diploma.data.dto.IndustryDTO
import ru.practicum.android.diploma.data.dto.IndustryList
import ru.practicum.android.diploma.domain.models.Resource
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApi
) : NetworkClient {

    override suspend fun searchResponse(request: Map<String, String>): Resource<VacanciesDTO> {
        if(!isConnected()) return Resource.ConnectionError(OFFLINE)
        return withContext(Dispatchers.IO) {
            try {
                return@withContext hhApi.getSearch(request).body()?.let { Resource.Data(it) } ?: Resource.NotFound(
                    OFFLINE)
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                return@withContext Resource.ConnectionError(REQUEST_ERROR_TAG)
            }
        }
    }

    override suspend fun getIndustry(): Resource<IndustryDTO> {
        if(!isConnected()) return Resource.ConnectionError(OFFLINE)
        return withContext(Dispatchers.IO) {
            try {
                return@withContext hhApi.getIndustry().body()?.let { Resource.Data(industryMapper(it)) } ?: Resource.NotFound(
                    NOT_FOUND)
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                return@withContext Resource.ConnectionError(REQUEST_ERROR_TAG)
            }
        }
    }

    override suspend fun getVacancyDetails(id: String): Resource<DetailsVacancyDTO> {
        if(!isConnected()) return Resource.ConnectionError(OFFLINE)
        return withContext(Dispatchers.IO) {
            try {
                return@withContext hhApi.getVacancyDetails(id).body()?.let { Resource.Data(it) } ?: Resource.NotFound(
                    NOT_FOUND)
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                return@withContext Resource.ConnectionError(REQUEST_ERROR_TAG)
            }
        }
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
        private const val NOT_FOUND = "not found"
        private const val OFFLINE = "no internet"
    }
}
