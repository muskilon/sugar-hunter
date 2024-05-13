package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface HHApi {
    @GET("vacancies")
    suspend fun getSearch(
        @Query("text") text: String
    ): SearchResponse
}
