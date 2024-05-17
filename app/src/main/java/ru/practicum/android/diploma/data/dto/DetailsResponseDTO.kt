package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class DetailsResponse(
    val id: String,
    val name: String,
    val area: AreaDTO,
    val employer: EmployerDTO,
    val salary: SalaryDTO?,
    @SerializedName("alternate_url") val url: String,
    val experience: ExperienceDTO?,
    val employment: EmploymentDTO?,
    val schedule: ScheduleDTO?,
    val description: String?,
    @SerializedName("key_skills") val keySkills: List<KeySkillsDTO>?,
    val contacts: ContactsDTO?
)

data class ExperienceDTO(
    val id: String,
    val name: String
)

data class EmploymentDTO(
    val id: String,
    val name: String
)
data class ScheduleDTO(
    val id: String,
    val name: String
)

data class ContactsDTO(
    val email: String?,
    val name: String?,
    val phones: List<PhonesDTO>?
)

data class PhonesDTO(
    val city: String?,
    val comment: String?,
    val country: String?,
    val number: String?,
    val formatted: String?
)

data class KeySkillsDTO(
    val name: String?
)
