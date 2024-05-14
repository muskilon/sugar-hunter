package ru.practicum.android.diploma.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.LogoUrls
import ru.practicum.android.diploma.data.dto.Salary

class ConvertType {

    @TypeConverter
    fun stringToLogoUrls(value: String): LogoUrls? {
        return Gson().fromJson(value, LogoUrls::class.java)
    }

    @TypeConverter
    fun logoUrlsToString(logos: LogoUrls?): String {
        return Gson().toJson(logos)
    }

    @TypeConverter
    fun fromString(value: String): Salary? {
        return Gson().fromJson(value, Salary::class.java)
    }

    @TypeConverter
    fun salaryToString(salary: Salary?): String {
        return Gson().toJson(salary)
    }

}
