package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.Area
import ru.practicum.android.diploma.data.dto.Employer
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.data.network.Contacts
import ru.practicum.android.diploma.data.network.Employment
import ru.practicum.android.diploma.data.network.Experience
import ru.practicum.android.diploma.data.network.KeySkills
import ru.practicum.android.diploma.data.network.Schedule

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
    val keySkills: List<KeySkills>?,
    val contacts: Contacts?
)
