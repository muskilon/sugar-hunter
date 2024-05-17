package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.network.responses.DetailsResponse
import ru.practicum.android.diploma.data.network.responses.IndustryListDTO
import ru.practicum.android.diploma.data.network.responses.SearchResponse

interface HHApi {
    @GET("vacancies")
    suspend fun getSearch(
        @QueryMap options: Map<String, String>
    ): SearchResponse

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): DetailsResponse

    @GET("industries")
    suspend fun getIndustry(): Array<IndustryListDTO>
}
