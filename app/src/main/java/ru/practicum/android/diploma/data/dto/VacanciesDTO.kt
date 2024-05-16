package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacanciesDTO(
    val id: String,
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?,
    val page: Int,
    val pages: Int,
    val found: Int
)

data class Area(
    val name: String
)

data class Employer(
    val name: String,
    @SerializedName("logo_urls") val logoUrls: LogoUrls?
)

data class LogoUrls(
    @SerializedName("90") val logo90: String?,
    @SerializedName("240") val logo240: String?
)

data class Salary(
    val from: Long?,
    val to: Long?,
    val currency: String,
    val gross: Boolean?
)
