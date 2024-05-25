package ru.practicum.android.diploma.ui.filter.place

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.domain.models.Areas

class ChoicePlaceViewHolder(
    private val binding: ItemCountryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Areas) {
        binding.textCountry.text = model.name
    }
}
