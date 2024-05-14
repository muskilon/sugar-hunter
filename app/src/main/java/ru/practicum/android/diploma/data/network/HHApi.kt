package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.network.responses.DetailsResponse
import ru.practicum.android.diploma.data.network.responses.IndustryList
import ru.practicum.android.diploma.data.network.responses.SearchResponse

interface HHApi {
    @GET("vacancies")
    suspend fun getSearch(
        @Query("text") text: String
    ): SearchResponse

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): DetailsResponse

    @GET("industries")
    suspend fun getIndustry(): Array<IndustryList>
}
