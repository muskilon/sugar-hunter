package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDictionaryDTO(
    val areas: List<AreaDictionaryDTO>,
    val id : String?,
    val name : String?,
    @SerializedName("parent_id") val parentId : String?
)
data class AreasDTO(
    val container: List<AreaDictionaryDTO>
)
