package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.IndustryInteractor
import ru.practicum.android.diploma.domain.IndustryRepository
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Resource

class IndustryInteractorImpl(
    private val repository: IndustryRepository
) : IndustryInteractor {

    override suspend fun getIndustries(): Flow<Resource<List<Industries>>> = repository.getIndustries()

}
