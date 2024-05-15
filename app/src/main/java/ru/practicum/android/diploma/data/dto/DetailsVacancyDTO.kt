package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.KeySkills
import ru.practicum.android.diploma.domain.models.Schedule

data class DetailsVacancyDTO(
    val id: String,
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?,
    val url: String,
    val experience: Experience?,
    val employment: Employment?,
    val schedule: Schedule?,
    val description: String?,
    @SerializedName("key_skills") val keySkills: List<KeySkills>?,
    val contacts: Contacts?
)
