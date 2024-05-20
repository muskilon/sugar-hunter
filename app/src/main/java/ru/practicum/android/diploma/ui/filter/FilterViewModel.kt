package ru.practicum.android.diploma.ui.filter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.FiltresInterActor
import ru.practicum.android.diploma.domain.models.SavedFiltres

class FilterViewModel(
    val filtresInterActor: FiltresInterActor
) : ViewModel() {

    val mutable = MutableLiveData<Int>()

    fun setFiltres(filtres: SavedFiltres){
        filtresInterActor.updateFiltres(filtres)
    }
    fun getFiltres(){
        val ttt = filtresInterActor.getFiltres()
        Log.d("FILTRES", ttt.toString())
    }
}
