package ru.practicum.android.diploma.domain.models

data class AreaDictionary(
    val areas: List<AreaDictionary>,
    val id : String?,
    val name : String?,
    val parentId : String?
)
