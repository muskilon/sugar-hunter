package ru.practicum.android.diploma.ui.filter.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryAdapter(private val clickListener: IndustryClickListener) : RecyclerView.Adapter<IndustryViewHolder>() {

    var industryList = ArrayList<Industry>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return IndustryViewHolder(ItemIndustryBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return industryList.size
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industryList[position])
        holder.itemView.setOnClickListener {
            clickListener.onIndustryClick(industryList[position])
        }
    }

    fun interface IndustryClickListener {
        fun onIndustryClick(industry: Industry)
    }

}

