package ru.practicum.android.diploma.ui.filter.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Industry

class IndustryAdapter(private val listener: OnItemSelectedListener) : RecyclerView.Adapter<IndustryViewHolder>() {

    var industryList = ArrayList<Industries>()

    val listPair: ArrayList<Pair<Industries, Boolean>> = industryList.map {
        Pair(it, false)
    } as ArrayList

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
            listener.onItemSelected(industryList[position])
        }
    }

 /*   fun selectIndustry(industryList: ArrayList<Pair<Industries, Boolean>>, selectedIndustry: Industries) {
        for (pair in industryList) {
            if (pair.first == selectedIndustry) {
                industryList.forEach {
                    it.second = (it.first == selectedIndustry)
                }
            }
        }
    }*/

    fun interface OnItemSelectedListener {
        fun onItemSelected(industries: Industries)
    }

}

