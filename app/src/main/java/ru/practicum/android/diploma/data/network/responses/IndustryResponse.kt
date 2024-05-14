package ru.practicum.android.diploma.data.network.responses

import ru.practicum.android.diploma.data.network.Response

data class IndustryResponse(
    val container: List<IndustryList>
) : Response()
class IndustryList(
    val id: String,
    val industries: List<Industry>,
    val name: String
)
data class Industry(
    val id: String,
    val name: String
)
