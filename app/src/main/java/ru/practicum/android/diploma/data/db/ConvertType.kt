package ru.practicum.android.diploma.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.EmployerDTO
import ru.practicum.android.diploma.data.dto.SalaryDTO
import ru.practicum.android.diploma.data.network.responses.ContactsDTO
import ru.practicum.android.diploma.data.network.responses.EmploymentDTO
import ru.practicum.android.diploma.data.network.responses.ExperienceDTO
import ru.practicum.android.diploma.data.network.responses.ScheduleDTO

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
    fun stringToLogoUrls(value: String): EmployerDTO? {
        return Gson().fromJson(value, EmployerDTO::class.java)
    }

    @TypeConverter
    fun logoUrlsToString(employer: EmployerDTO?): String {
        return Gson().toJson(employer)
    }

    @TypeConverter
    fun fromString(value: String): SalaryDTO? {
        return Gson().fromJson(value, SalaryDTO::class.java)
    }

    @TypeConverter
    fun salaryToString(salary: SalaryDTO?): String {
        return Gson().toJson(salary)
    }

    @TypeConverter
    fun stringToExperience(value: String): ExperienceDTO? {
        return Gson().fromJson(value, ExperienceDTO::class.java)
    }

    @TypeConverter
    fun experienceToString(experience: ExperienceDTO?): String {
        return Gson().toJson(experience)
    }

    @TypeConverter
    fun stringToEmployment(value: String): EmploymentDTO? {
        return Gson().fromJson(value, EmploymentDTO::class.java)
    }

    @TypeConverter
    fun employmentToString(employment: EmploymentDTO?): String {
        return Gson().toJson(employment)
    }

    @TypeConverter
    fun stringToSchedule(value: String): ScheduleDTO? {
        return Gson().fromJson(value, ScheduleDTO::class.java)
    }

    @TypeConverter
    fun scheduleToString(schedule: ScheduleDTO?): String {
        return Gson().toJson(schedule)
    }

    @TypeConverter
    fun stringToContacts(value: String): ContactsDTO? {
        return Gson().fromJson(value, ContactsDTO::class.java)
    }

    @TypeConverter
    fun contactsToString(contacts: ContactsDTO?): String {
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
