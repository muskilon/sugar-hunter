package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreasDictionaryDTO(
    val container: List<AreaItemDTO>
)
data class AreaItemDTO(
    val areas: List<AreaItemDTO> = listOf(),
    val id: String,
    val name: String,
    @SerializedName("parent_id")
    val parentId: String?
)
