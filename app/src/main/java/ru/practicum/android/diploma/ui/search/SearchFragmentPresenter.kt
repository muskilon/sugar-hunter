package ru.practicum.android.diploma.ui.search

import android.util.Log
import androidx.core.view.isVisible
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.app.App
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.ui.Key
import ru.practicum.android.diploma.ui.search.recyclerview.SearchAdapter

class SearchFragmentPresenter(val binding: FragmentSearchBinding) {
    fun showStart() {
        with(binding) {
            placeholderSearch.isVisible = true
            noInternet.isVisible = false
            somethingWrong.isVisible = false
            progressBar.isVisible = false
            vacancyCount.isVisible = false
            searchRecyclerView.isVisible = false
        }
    }
    fun showLoading() {
        with(binding) {
            placeholderSearch.isVisible = false
            noInternet.isVisible = false
            somethingWrong.isVisible = false
            progressBar.isVisible = true
            vacancyCount.isVisible = false
            searchRecyclerView.isVisible = false
            searchRecyclerView.removeAllViewsInLayout()
        }
    }
    fun showError(errorMessage: String) {
        with(binding) {
            placeholderSearch.isVisible = false
            noInternet.isVisible = true
            somethingWrong.isVisible = false
            progressBar.isVisible = false
            vacancyCount.isVisible = false
            searchRecyclerView.isVisible = false
        }
        Log.d(Key.ERROR_MESSAGE, errorMessage)
    }
    fun showEmpty(emptyMessage: String) {
        with(binding) {
            placeholderSearch.isVisible = false
            noInternet.isVisible = false
            somethingWrong.isVisible = true
            progressBar.isVisible = false
            vacancyCount.isVisible = true
            searchRecyclerView.isVisible = false
            vacancyCount.text = App.getAppResources()?.getString(
                R.string.search_error_no_vacancies
            )
        }
        Log.d(Key.ERROR_MESSAGE, emptyMessage)
    }
    fun showContent(vacancy: VacanciesResponse, totalFoundVacancies: Int, searchAdapter: SearchAdapter) {
        searchAdapter.setData(vacancy.items)
        with(binding) {
            placeholderSearch.isVisible = false
            noInternet.isVisible = false
            somethingWrong.isVisible = false
            progressBar.isVisible = false
            vacancyCount.isVisible = true
            searchRecyclerView.isVisible = true
            pageLoading.isVisible = false
            vacancyCount.text = App.getAppResources()?.getQuantityString(
                R.plurals.vacancy_plurals, totalFoundVacancies, totalFoundVacancies
            )
        }
    }
    fun justShowContent(totalFoundVacancies: Int) {
        with(binding) {
            placeholderSearch.isVisible = false
            noInternet.isVisible = false
            somethingWrong.isVisible = false
            progressBar.isVisible = false
            vacancyCount.isVisible = true
            searchRecyclerView.isVisible = true
            pageLoading.isVisible = false
            vacancyCount.text = App.getAppResources()?.getQuantityString(
                R.plurals.vacancy_plurals, totalFoundVacancies, totalFoundVacancies
            )

        }
    }
}
