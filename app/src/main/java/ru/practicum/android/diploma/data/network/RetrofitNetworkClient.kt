package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.DetailsVacancyDTO
import ru.practicum.android.diploma.data.dto.IndustryDTO
import ru.practicum.android.diploma.data.dto.VacanciesDTO
import ru.practicum.android.diploma.domain.models.Resource
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApi: HHApi
) : NetworkClient {

    override suspend fun getVacancies(request: Map<String, String>): Resource<VacanciesDTO> {
        var vacancies: Resource<VacanciesDTO>
        if (!isConnected()) return Resource.ConnectionError(OFFLINE)
        withContext(Dispatchers.IO) {
            vacancies = try {
                hhApi.getSearch(request).body()?.let { Resource.Data(it) } ?: Resource.NotFound(
                    OFFLINE)
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                Resource.ConnectionError(REQUEST_ERROR_TAG)
            }
        }
        return vacancies
    }

    override suspend fun getIndustry(): Resource<IndustryDTO> {
        var industry: Resource<IndustryDTO>
        if (!isConnected()) return Resource.ConnectionError(OFFLINE)
        withContext(Dispatchers.IO) {
            industry = try {
                hhApi.getIndustry().body()?.let {
                    Resource.Data(IndustryDTO(it.asList()))
                } ?: Resource.NotFound(
                    NOT_FOUND)
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                Resource.ConnectionError(REQUEST_ERROR_TAG)
            }
        }
        return industry
    }

    override suspend fun getVacancyDetails(id: String): Resource<DetailsVacancyDTO> {
        var details: Resource<DetailsVacancyDTO>
        if (!isConnected()) return Resource.ConnectionError(OFFLINE)
        withContext(Dispatchers.IO) {
            details = try {
                hhApi.getVacancyDetails(id).body()?.let { Resource.Data(it) } ?: Resource.NotFound(
                    NOT_FOUND)
            } catch (ex: IOException) {
                Log.e(REQUEST_ERROR_TAG, ex.toString())
                Resource.ConnectionError(REQUEST_ERROR_TAG)
            }
        }
        return details
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
