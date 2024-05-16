package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.EmployerDTO
import ru.practicum.android.diploma.data.dto.SalaryDTO
import ru.practicum.android.diploma.data.network.responses.ContactsDTO
import ru.practicum.android.diploma.data.network.responses.EmploymentDTO
import ru.practicum.android.diploma.data.network.responses.ExperienceDTO
import ru.practicum.android.diploma.data.network.responses.ScheduleDTO

data class VacancyDetails(
    val id: String,
    val title: String,
    val area: AreaDTO,
    val employer: EmployerDTO,
    val salary: SalaryDTO?,
    val experience: ExperienceDTO?,
    val employment: EmploymentDTO?,
    val schedule: ScheduleDTO?,
    val description: String?,
    val keySkills: List<String>?,
    val contacts: ContactsDTO?
)
