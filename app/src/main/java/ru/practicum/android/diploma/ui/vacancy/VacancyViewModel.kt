package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyViewModel(
    private val vacancy: Vacancy,
    private val favouriteDataBaseInteractor: FavouriteDataBaseInteractor
) : ViewModel() {

    val mutable = MutableLiveData<Int>()

}
