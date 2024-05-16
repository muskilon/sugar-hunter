package ru.practicum.android.diploma.domain.models

data class VacancyDetails(
    val id: String,
    val title: String,
    val city: String?,
    val employer: String,
    val salary: Salary?,
    val experience: Experience?,
    val employment: Employment?,
    val schedule: Schedule?,
    val description: String?,
    val keySkills: List<String>?,
    val contacts: Contacts?,
    val logoUrls: LogoUrls
)
data class Experience(
    val id: String?,
    val name: String?
)

data class Employment(
    val id: String?,
    val name: String?
)
data class Schedule(
    val id: String?,
    val name: String?
)

data class Contacts(
    val email: String?,
    val name: String?,
    val phones: List<Phones>?
)

data class Phones(
    val city: String?,
    val comment: String?,
    val country: String?,
    val number: String?,
    val formatted: String?
)
