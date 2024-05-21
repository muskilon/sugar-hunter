package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacanciesDTO(
    val id: String,
    val name: String,
    val area: AreaDTO,
    val employer: EmployerDTO,
    val salary: SalaryDTO?,
    val page: Int,
    val pages: Int,
    val found: Int
)

data class AreaDTO(
    val name: String
)

data class EmployerDTO(
    val name: String,
    @SerializedName("logo_urls")
    val logoUrls: LogoUrlsDTO?
)

data class LogoUrlsDTO(
    @SerializedName("90")
    val logo90: String?,
    @SerializedName("240")
    val logo240: String?
)

data class SalaryDTO(
    val from: Long?,
    val to: Long?,
    val currency: String,
    val gross: Boolean?
)
