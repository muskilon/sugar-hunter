package ru.practicum.android.diploma.data.db

import ru.practicum.android.diploma.domain.models.Vacancy

class Convertor {

    fun mapFromVacancy(vacancy: Vacancy): FavouriteVacancy {
        return FavouriteVacancy(
            vacancy.id,
            vacancy.title,
            vacancy.city,
            vacancy.employer,
            vacancy.logos,
            vacancy.salary
        )
    }

    fun mapFromFavourite(vacancy: FavouriteVacancy): Vacancy {
        return Vacancy(
            vacancy.id,
            vacancy.title,
            vacancy.city,
            vacancy.employer,
            vacancy.logos,
            vacancy.salary
        )
    }

}
