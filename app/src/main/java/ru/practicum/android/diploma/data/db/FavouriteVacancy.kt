package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.data.dto.LogoUrls
import ru.practicum.android.diploma.data.dto.Salary

@Entity(tableName = "favourite_vacancy") //  placeholder
data class FavouriteVacancy(
    @PrimaryKey
    val id: String,
    val title: String,
    val city: String,
    val employer: String,
    val logos: LogoUrls?,
    val salary: Salary?
)
