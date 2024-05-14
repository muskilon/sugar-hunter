package ru.practicum.android.diploma.ui.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.db.FavouriteDataBaseInteractor

class FavouriteViewModel(private val favouriteDataBaseInteractor: FavouriteDataBaseInteractor) : ViewModel() {

    val mutable = MutableLiveData<Int>()

}
