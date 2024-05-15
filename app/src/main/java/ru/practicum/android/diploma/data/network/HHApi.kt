package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.DTOVacancies
import ru.practicum.android.diploma.data.dto.IndustryList
import ru.practicum.android.diploma.data.network.responses.DetailsResponse

interface HHApi {
    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): DetailsResponse

    @GET("industries")
    suspend fun getIndustry(): Response<Array<IndustryList>>

    @GET("vacancies")
    suspend fun getSearch(
        @QueryMap options: Map<String, String>
    ): Response<DTOVacancies>
}
