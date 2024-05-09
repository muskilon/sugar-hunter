package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_vacancy") //  placeholder
data class FavouriteVacancy(
    @PrimaryKey
    val id: String
)
