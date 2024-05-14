package ru.practicum.android.diploma.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouriteAdapter(private val clickListener: VacancyClickListener) :
    RecyclerView.Adapter<FavouriteViewHolder>() {

    var favoriteList = ArrayList<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vacancy, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
        holder.itemView.setOnClickListener {
            clickListener.onAlbumClick(favoriteList[position])
        }
    }

    fun interface VacancyClickListener {
        fun onAlbumClick(vacancy: Vacancy)
    }

}
