package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.IndustryRepositoryImpl
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.db.FavouriteDataBaseRepositoryImpl
import ru.practicum.android.diploma.data.filtres.FiltersRepositoryImpl
import ru.practicum.android.diploma.domain.FiltersInterActor
import ru.practicum.android.diploma.domain.FiltersRepository
import ru.practicum.android.diploma.domain.IndustryInteractor
import ru.practicum.android.diploma.domain.IndustryRepository
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractorImpl
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseRepository
import ru.practicum.android.diploma.domain.impl.FiltersInterActorImpl
import ru.practicum.android.diploma.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesInterActorImpl

val domainModules = module {
    factory<VacanciesInterActor> { VacanciesInterActorImpl(repository = get()) }
    single<VacanciesRepository> { VacanciesRepositoryImpl(networkClient = get(), mapper = get()) }

    single<FavouriteDataBaseRepository> { FavouriteDataBaseRepositoryImpl(database = get(), convertor = get()) }
    single<FavouriteDataBaseInteractor> { FavouriteDataBaseInteractorImpl(favouriteDataBaseRepository = get()) }

    factory<FiltersInterActor> { FiltersInterActorImpl(repository = get()) }
    single<FiltersRepository> { FiltersRepositoryImpl(filtersStorage = get()) }

    factory<IndustryInteractor> { IndustryInteractorImpl(repository = get()) }
    single<IndustryRepository> { IndustryRepositoryImpl(networkClient = get(), mapper = get()) }
}
