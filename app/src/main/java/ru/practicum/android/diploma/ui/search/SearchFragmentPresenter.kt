package ru.practicum.android.diploma.ui.search

import android.view.View
import androidx.core.view.isVisible
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.app.App
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.ui.search.recyclerview.SearchAdapter

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
    }
    fun showEmpty(emptyMessage: String) {
        with(binding) {
            vacancyCount.text = App.getAppResources()?.getString(
                R.string.search_error_no_vacancies
            )
            vacancyCount.visibility = View.VISIBLE
            somethingWrong.visibility = View.VISIBLE
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            noInternet.visibility = View.GONE
        }
    }
    fun showContent(vacancy: VacanciesResponse, totalFoundVacancies: Int, searchAdapter: SearchAdapter): Boolean {
        searchAdapter.setData(vacancy.items)
        with(binding) {
            vacancyCount.text = App.getAppResources()?.getQuantityString(
                R.plurals.vacancy_plurals, totalFoundVacancies, totalFoundVacancies
            )
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            noInternet.visibility = View.GONE
            vacancyCount.visibility = View.VISIBLE
            searchRecyclerView.visibility = View.VISIBLE
            pageLoading.isVisible = false
        }
        return false
    }
    fun justShowContent(totalFoundVacancies: Int): Boolean {
        with(binding) {
            vacancyCount.text = App.getAppResources()?.getQuantityString(
                R.plurals.vacancy_plurals, totalFoundVacancies, totalFoundVacancies
            )
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            noInternet.visibility = View.GONE
            vacancyCount.visibility = View.VISIBLE
            searchRecyclerView.visibility = View.VISIBLE
            pageLoading.isVisible = false
        }
        return false
    }
}
