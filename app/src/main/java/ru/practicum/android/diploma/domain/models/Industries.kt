package ru.practicum.android.diploma.domain.models

data class Industries(
    val id: String?,
    val name: String?,
    val industries: List<Industry>
)
class IndustryList(
    val id: String,
    val industries: List<Industry>,
    val name: String
)
data class Industry(
    val id: String,
    val name: String
)
