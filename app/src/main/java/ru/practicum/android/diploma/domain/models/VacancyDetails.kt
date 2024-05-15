package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.Area
import ru.practicum.android.diploma.data.dto.Employer
import ru.practicum.android.diploma.data.dto.Salary

data class VacancyDetails(
    val id: String,
    val title: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?,
    val experience: Experience?,
    val employment: Employment?,
    val schedule: Schedule?,
    val description: String?,
    val keySkills: List<String>?,
    val contacts: Contacts?
)
data class Experience(
    val id: String,
    val name: String
)

data class Employment(
    val id: String,
    val name: String
)
data class Schedule(
    val id: String,
    val name: String
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

data class KeySkills(
    val name: String?
)
