package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [FavouriteVacancy::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritesVacanciesDao(): FavouritesVacanciesDao

}