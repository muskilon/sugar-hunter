package ru.practicum.android.diploma.domain.models

data class Industries(
    val id: String?,
    val name: String?,
    val industries: List<Industry>
)
data class Industry(
    val id: String,
    val name: String
)
