package ru.practicum.android.diploma.ui.filter.place.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.domain.models.Areas

class CountryAdapter(
    private val onItemClick: (Areas) -> Unit
) : RecyclerView.Adapter<CountryViewHolder>() {

    private val countries = ArrayList<Areas>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return CountryViewHolder(ItemCountryBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(countries[holder.getBindingAdapterPosition()])
        }
    }

    override fun getItemCount(): Int = countries.size

    fun setData(newCountries: List<Areas>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyItemRangeInserted(0, newCountries.size)
    }
}
