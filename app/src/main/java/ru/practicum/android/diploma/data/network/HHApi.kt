package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.DetailsResponse
import ru.practicum.android.diploma.data.dto.IndustryListDTO
import ru.practicum.android.diploma.data.dto.SearchResponseDTO

interface HHApi {
    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(
        @Path("vacancy_id") id: String
    ): Response<DetailsResponse>

    @GET("industries")
    suspend fun getIndustry(): Response<Array<IndustryListDTO>>

    @GET("vacancies")
    suspend fun getSearch(
        @QueryMap options: Map<String, String>
    ): Response<SearchResponseDTO>
}
