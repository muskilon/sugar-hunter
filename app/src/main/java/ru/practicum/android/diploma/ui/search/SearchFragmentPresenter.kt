package ru.practicum.android.diploma.ui.search

import android.util.Log
import android.view.View
import ru.practicum.android.diploma.databinding.FragmentSearchBinding

class SearchFragmentPresenter(val binding: FragmentSearchBinding) {
    fun showStart() {
        with(binding) {
            placeholderSearch.visibility = View.VISIBLE
            noInternet.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            progressBar.visibility = View.GONE
            vacancyCount.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
        }
    }

    fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            placeholderSearch.visibility = View.GONE
            noInternet.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            vacancyCount.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            searchRecyclerView.removeAllViewsInLayout()
        }
    }

    fun showError(errorMessage: String) {
        with(binding) {
            noInternet.visibility = View.VISIBLE
            placeholderSearch.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            vacancyCount.visibility = View.GONE
        }
        Log.d("errorMessage: ", errorMessage)
    }
}
