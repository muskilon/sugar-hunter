package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.AreaItemDTO

data class AreaDictionary(
    val container: List<AreaItemDTO>
)
data class AreaItem(
    val areas: List<AreaItem>?,
    val id : String,
    val name : String,
    val parentId : String?
)
