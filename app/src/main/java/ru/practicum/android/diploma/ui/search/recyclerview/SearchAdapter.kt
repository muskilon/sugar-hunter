package ru.practicum.android.diploma.ui.search.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchAdapter(
    private val onItemClick: (Vacancy) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>() {

    private val vacancy = ArrayList<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return SearchViewHolder(ItemVacancyBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(vacancy[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(vacancy[holder.getBindingAdapterPosition()])
        }
    }

    override fun getItemCount(): Int = vacancy.size

    fun setData(newVacancies: List<Vacancy>) {
        if (newVacancies.size < vacancy.size) {
            notifyItemRangeRemoved(0, vacancy.size)
        }
        vacancy.clear()
        vacancy.addAll(newVacancies)
        notifyItemRangeInserted(vacancy.size + 1, newVacancies.size)
    }
}
