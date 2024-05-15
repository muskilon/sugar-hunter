package ru.practicum.android.diploma.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.Area
import ru.practicum.android.diploma.data.dto.Employer
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.data.network.responses.Contacts
import ru.practicum.android.diploma.data.network.responses.Employment
import ru.practicum.android.diploma.data.network.responses.Experience
import ru.practicum.android.diploma.data.network.responses.Schedule

class ConvertType {

    @TypeConverter
    fun stringToArea(value: String): Area? {
        return Gson().fromJson(value, Area::class.java)
    }

    @TypeConverter
    fun areaToString(area: Area?): String {
        return Gson().toJson(area)
    }

    @TypeConverter
    fun stringToLogoUrls(value: String): Employer? {
        return Gson().fromJson(value, Employer::class.java)
    }

    @TypeConverter
    fun logoUrlsToString(employer: Employer?): String {
        return Gson().toJson(employer)
    }

    @TypeConverter
    fun fromString(value: String): Salary? {
        return Gson().fromJson(value, Salary::class.java)
    }

    @TypeConverter
    fun salaryToString(salary: Salary?): String {
        return Gson().toJson(salary)
    }

    @TypeConverter
    fun stringToExperience(value: String): Experience? {
        return Gson().fromJson(value, Experience::class.java)
    }

    @TypeConverter
    fun experienceToString(experience: Experience?): String {
        return Gson().toJson(experience)
    }

    @TypeConverter
    fun stringToEmployment(value: String): Employment? {
        return Gson().fromJson(value, Employment::class.java)
    }

    @TypeConverter
    fun employmentToString(employment: Employment?): String {
        return Gson().toJson(employment)
    }

    @TypeConverter
    fun stringToSchedule(value: String): Schedule? {
        return Gson().fromJson(value, Schedule::class.java)
    }

    @TypeConverter
    fun scheduleToString(schedule: Schedule?): String {
        return Gson().toJson(schedule)
    }

    @TypeConverter
    fun stringToContacts(value: String): Contacts? {
        return Gson().fromJson(value, Contacts::class.java)
    }

    @TypeConverter
    fun contactsToString(contacts: Contacts?): String {
        return Gson().toJson(contacts)
    }

    @TypeConverter
    fun stringToList(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return list.joinToString(",")
    }

}
