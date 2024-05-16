package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val title: String,
    val city: String?,
    val employer: String,
    val logos: LogoUrls?,
    val salary: Salary?,
)
data class Salary(
    val from: Long?,
    val to: Long?,
    val currency: String?,
    val gross: Boolean?
)
data class LogoUrls(
    val logo90: String?,
    val logo240: String?
)
