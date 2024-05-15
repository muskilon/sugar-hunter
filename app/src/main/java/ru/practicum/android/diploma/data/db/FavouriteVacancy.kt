package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.data.dto.Area
import ru.practicum.android.diploma.data.dto.Employer
import ru.practicum.android.diploma.data.dto.LogoUrls
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.data.network.responses.Contacts
import ru.practicum.android.diploma.data.network.responses.Employment
import ru.practicum.android.diploma.data.network.responses.Experience
import ru.practicum.android.diploma.data.network.responses.Schedule

@Entity(tableName = "favourite_vacancy") //  placeholder
data class FavouriteVacancy(
    @PrimaryKey
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
