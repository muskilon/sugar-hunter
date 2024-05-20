package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.db.FavouriteDataBaseRepositoryImpl
import ru.practicum.android.diploma.data.filtres.FiltresRepositoryImpl
import ru.practicum.android.diploma.domain.FiltresInterActor
import ru.practicum.android.diploma.domain.FiltresRepository
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractorImpl
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseRepository
import ru.practicum.android.diploma.domain.impl.FiltresInterActorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesInterActorImpl

val domainModules = module {
    factory<VacanciesInterActor> { VacanciesInterActorImpl(repository = get()) }
    single<VacanciesRepository> { VacanciesRepositoryImpl(networkClient = get(), mapper = get()) }

    single<FavouriteDataBaseRepository> { FavouriteDataBaseRepositoryImpl(database = get(), convertor = get()) }
    single<FavouriteDataBaseInteractor> { FavouriteDataBaseInteractorImpl(favouriteDataBaseRepository = get()) }

    factory<FiltresInterActor> { FiltresInterActorImpl(repository = get()) }
    single<FiltresRepository> { FiltresRepositoryImpl(filtresStorage = get()) }
}
