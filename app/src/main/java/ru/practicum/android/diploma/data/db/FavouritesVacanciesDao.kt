package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouritesVacanciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: FavouriteVacancy)

    @Query("SELECT * FROM favourite_vacancy")
    suspend fun getVacancies(): List<FavouriteVacancy>

    @Delete(entity = FavouriteVacancy::class)
    fun deleteVacancy(vacancy: FavouriteVacancy)
}
