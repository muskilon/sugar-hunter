package ru.practicum.android.diploma.ui.filter.place

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.domain.models.Areas

class ChoicePlaceAdapter(
    private val onItemClick: (Areas) -> Unit
) : RecyclerView.Adapter<ChoicePlaceViewHolder>() {

    private val countries = ArrayList<Areas>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoicePlaceViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return ChoicePlaceViewHolder(ItemCountryBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: ChoicePlaceViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(countries[holder.getBindingAdapterPosition()])
        }
    }

    override fun getItemCount(): Int = countries.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newCountries: List<Areas>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }
}
