package ru.practicum.android.diploma.data.dto


data class IndustryDTO(
    val container: List<IndustryList>
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
