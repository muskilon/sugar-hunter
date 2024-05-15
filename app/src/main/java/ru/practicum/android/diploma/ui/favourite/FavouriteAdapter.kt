package ru.practicum.android.diploma.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.DiffUtilCallback

class FavouriteAdapter(private val clickListener: VacancyClickListener) :
    RecyclerView.Adapter<FavouriteViewHolder>() {

    var favoriteList = ArrayList<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vacancy, parent, false)
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavouriteViewHolder(view, ItemVacancyBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
        holder.itemView.setOnClickListener {
            clickListener.onVacancyClick(favoriteList[position])
        }
    }

    fun setData(newVacancies: List<Vacancy>) {
        val diffCallback = DiffUtilCallback(favoriteList, newVacancies)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        favoriteList.clear()
        favoriteList.addAll(newVacancies)
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }

}
