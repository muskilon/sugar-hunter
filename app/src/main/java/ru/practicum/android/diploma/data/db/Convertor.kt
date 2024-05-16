package ru.practicum.android.diploma.data.db

import ru.practicum.android.diploma.domain.models.VacancyDetails

class Convertor {

    fun mapFromVacancy(vacancy: VacancyDetails): FavouriteVacancy {
        return FavouriteVacancy(
            vacancy.id,
            vacancy.title,
            vacancy.area,
            vacancy.employer,
            vacancy.salary,
            vacancy.experience,
            vacancy.employment,
            vacancy.schedule,
            vacancy.description,
            vacancy.keySkills,
            vacancy.contacts
        )
    }

    fun mapFromFavourite(vacancy: FavouriteVacancy): VacancyDetails {
        return VacancyDetails(
            vacancy.id,
            vacancy.title,
            vacancy.area,
            vacancy.employer,
            vacancy.salary,
            vacancy.experience,
            vacancy.employment,
            vacancy.schedule,
            vacancy.description,
            vacancy.keySkills,
            vacancy.contacts
        )
    }

}
