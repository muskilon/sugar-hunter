package ru.practicum.android.diploma.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavouriteAdapter(private val clickListener: VacancyClickListener) :
    RecyclerView.Adapter<FavouriteViewHolder>() {

    var favoriteDetailsList = ArrayList<VacancyDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavouriteViewHolder(ItemVacancyBinding.inflate(layoutInspector, parent, false))
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

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: VacancyDetails)
    }

}
