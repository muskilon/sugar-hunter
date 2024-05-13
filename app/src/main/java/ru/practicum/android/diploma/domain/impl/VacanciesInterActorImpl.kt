package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.VacanciesInterActor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class VacanciesInterActorImpl(
    private val repository: VacanciesRepository
) : VacanciesInterActor {
    override fun searchVacancies(
        text: String
    ): Flow<Resource<List<Vacancy>>> {
        return repository.searchVacancies(text).map { result ->
            when (result) {
                is Resource.Data -> result
                is Resource.NotFound -> Resource.NotFound(EMPTY)
                is Resource.ConnectionError -> Resource.ConnectionError(EMPTY)
            }
        }
    }
    companion object {
        private const val EMPTY = ""
    }
}
