package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface HHApi { // placeholder
    @GET(".")
    suspend fun getSearch(
        @Query("query") entity: String
    ): SearchResponse
}
