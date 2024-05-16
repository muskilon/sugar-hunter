package ru.practicum.android.diploma.data.network.responses

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.EmployerDTO
import ru.practicum.android.diploma.data.dto.SalaryDTO
import ru.practicum.android.diploma.data.network.Response

data class DetailsResponse(
    val id: String,
    val name: String,
    val area: AreaDTO,
    val employer: EmployerDTO,
    val salary: SalaryDTO?,
    val url: String,
    val experience: ExperienceDTO?,
    val employment: EmploymentDTO?,
    val schedule: ScheduleDTO?,
    val description: String?,
    @SerializedName("key_skills") val keySkills: List<KeySkillsDTO>?,
    val contacts: ContactsDTO?
) : Response()

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
