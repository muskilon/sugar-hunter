package ru.practicum.android.diploma.domain.models

data class AreasDictionary(
    val container: List<AreaItem>
)
data class AreaItem(
    val areas: List<AreaItem>?,
    val id : String,
    val name : String,
    val parentId : String?
)
