package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.network.responses.Industry

data class Industries(
    val id: String?,
    val industries: List<Industry>,
    val name: String?
)
