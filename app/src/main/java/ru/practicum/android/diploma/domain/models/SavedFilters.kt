package ru.practicum.android.diploma.domain.models

data class SavedFilters(
    val filters: MutableMap<String, String> = mutableMapOf()
)
