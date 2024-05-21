package ru.practicum.android.diploma.data.dto

data class IndustryResponse(
    val container: List<IndustryListDTO>
)
data class IndustryListDTO(
    val id: String,
    val industries: List<IndustryDTO>,
    val name: String
)
data class IndustryDTO(
    val id: String,
    val name: String
)
