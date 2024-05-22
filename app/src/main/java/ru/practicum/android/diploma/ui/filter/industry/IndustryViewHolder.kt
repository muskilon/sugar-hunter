package ru.practicum.android.diploma.ui.filter.industry


import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Industries

class IndustryViewHolder(private val binding: ItemIndustryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(industry: Industries) {
        binding.itemCheck.text = industry.name
    }
}
