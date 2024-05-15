package ru.practicum.android.diploma.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.DiffUtilCallback

class FavouriteAdapter(private val clickListener: VacancyClickListener) :
    RecyclerView.Adapter<FavouriteViewHolder>() {

    var favoriteDetailsList = ArrayList<VacancyDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vacancy, parent, false)
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavouriteViewHolder(view, ItemVacancyBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return favoriteDetailsList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favoriteDetailsList[position])
        holder.itemView.setOnClickListener {
            clickListener.onVacancyClick(favoriteDetailsList[position])
        }
    }

    fun setData(newVacanciesDetails: List<VacancyDetails>) {
        val favoriteList = convertVacancyDetailsListToVacancyList(favoriteDetailsList)
        val newVacancies = convertVacancyDetailsListToVacancyList(newVacanciesDetails)
        val diffCallback = DiffUtilCallback(favoriteList, newVacancies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        favoriteDetailsList.clear()
        favoriteDetailsList.addAll(newVacanciesDetails)
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: VacancyDetails)
    }

    private fun convertVacancyDetailsListToVacancyList(vacancyDetailsList: List<VacancyDetails>): List<Vacancy> {
        val vacancyList = mutableListOf<Vacancy>()

        for (vacancyDetails in vacancyDetailsList) {
            val vacancy = Vacancy(
                id = vacancyDetails.id,
                title = vacancyDetails.title,
                city = vacancyDetails.area.name,
                employer = vacancyDetails.employer.name,
                logos = vacancyDetails.employer.logoUrls,
                salary = vacancyDetails.salary
            )

            vacancyList.add(vacancy)
        }

        return vacancyList
    }

}
