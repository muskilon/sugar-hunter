package ru.practicum.android.diploma.ui.filter.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Vacancy

class IndustryAdapter(private val onItemClick: (Industries) -> Unit) : RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder>() {

    var industryList = ArrayList<Industries>()

    var itemSelected = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return IndustryViewHolder(ItemIndustryBinding.inflate(layoutInspector, parent, false))
    }


    override fun getItemCount(): Int {
        return industryList.size
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = industryList[position]
        holder.binding.itemCheck.text = industry.name
        if (itemSelected == position) {
            holder.selected()
        } else {
            holder.unselected()
        }
    }

    inner class IndustryViewHolder(val binding: ItemIndustryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemCheck.setOnClickListener {
                selection(bindingAdapterPosition)
                onItemClick.invoke(industryList[bindingAdapterPosition])
            }
        }

        fun bind(industry: Industries) {
            binding.itemCheck.text = industry.name
        }

        fun selected() {
            binding.itemCheck.isChecked = true
        }

        fun unselected() {
            binding.itemCheck.isChecked = false
        }

        private fun selection(adapterPosition: Int) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            notifyItemChanged(itemSelected)
            itemSelected = bindingAdapterPosition
            notifyItemChanged(itemSelected)
        }
    }

}


