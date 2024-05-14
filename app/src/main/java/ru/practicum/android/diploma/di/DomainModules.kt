package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.impl.VacanciesInterActorImpl
import ru.practicum.android.diploma.domain.impl.VacanciesRepositoryImpl

val domainModules = module {
    factory<VacanciesInterActor> { VacanciesInterActorImpl(repository = get()) }
    single<VacanciesRepository> { VacanciesRepositoryImpl(networkClient = get()) }
}
