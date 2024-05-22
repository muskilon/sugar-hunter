package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouritesVacanciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVacancy(vacancy: FavouriteVacancy)

    @Query("SELECT * FROM favourite_vacancy")
    fun getVacancies(): List<FavouriteVacancy>

    @Delete(entity = FavouriteVacancy::class)
    fun deleteVacancy(vacancy: FavouriteVacancy)

    @Query("SELECT id FROM favourite_vacancy")
    fun getFavoritesIds(): List<String>
}
