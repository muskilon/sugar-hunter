package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.LogoUrls
import ru.practicum.android.diploma.data.dto.Salary

data class Vacancy(
    val id: String,
    val title: String,
    val city: String,
    val employer: String,
    val logos: LogoUrls?,
    val salary: Salary?
)
