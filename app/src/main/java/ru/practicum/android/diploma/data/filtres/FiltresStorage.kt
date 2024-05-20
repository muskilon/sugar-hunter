package ru.practicum.android.diploma.data.filtres

import android.content.SharedPreferences

class FiltresStorage(private val sharedPreferences: SharedPreferences) {

    fun getFitres(): String?{
        return sharedPreferences.getString(FILTRES, null)
    }

    fun updateFiltes(filtres: String){
        sharedPreferences.edit()
            .putString(FILTRES, filtres)
            .apply()
    }
    companion object{
        const val FILTRES = "filtres"
    }
}
