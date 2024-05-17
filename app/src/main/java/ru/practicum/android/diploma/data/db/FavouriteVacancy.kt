package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.LogoUrls
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule

@Entity(tableName = "favourite_vacancy") //  placeholder
data class FavouriteVacancy(
    @PrimaryKey
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
    val logoUrls: LogoUrls,
    val url: String
)
