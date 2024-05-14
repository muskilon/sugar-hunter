package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.DiffUtilCallback

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
            onItemClick.invoke(vacancy[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = vacancy.size

    fun setData(newTracks: List<Vacancy>) {
        val diffCallback = DiffUtilCallback(vacancy, newTracks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        vacancy.clear()
        vacancy.addAll(newTracks)
        diffResult.dispatchUpdatesTo(this)
    }
}
