package ru.practicum.android.diploma.domain.models

sealed interface FavouritesState {
    data class Content(
        val favourites: ArrayList<Vacancy>
    ) : FavouritesState

    data object Error : FavouritesState

    data object Empty : FavouritesState

}
