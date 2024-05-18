package ru.practicum.android.diploma.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.domain.models.Address
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.LogoUrls
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule

class ConvertType {

    @TypeConverter
    fun stringToArea(value: String): AreaDTO? {
        return Gson().fromJson(value, AreaDTO::class.java)
    }

    @TypeConverter
    fun areaToString(area: AreaDTO?): String {
        return Gson().toJson(area)
    }

    @TypeConverter
    fun stringToLogoUrls(value: String): LogoUrls? {
        return Gson().fromJson(value, LogoUrls::class.java)
    }

    @TypeConverter
    fun logoUrlsToString(employer: LogoUrls?): String {
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
    fun stringToAddress(value: String): Address? {
        return Gson().fromJson(value, Address::class.java)
    }

    @TypeConverter
    fun addressToString(address: Address?): String {
        return Gson().toJson(address)
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
