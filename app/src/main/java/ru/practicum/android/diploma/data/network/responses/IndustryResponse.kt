package ru.practicum.android.diploma.data.network.responses

import ru.practicum.android.diploma.data.network.Response

data class IndustryResponse(
    val container: List<IndustryListDTO>
) : Response()
class IndustryListDTO(
    val id: String,
    val industries: List<IndustryDTO>,
    val name: String
)
data class IndustryDTO(
    val id: String,
    val name: String
)
