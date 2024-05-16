package ru.practicum.android.diploma.ui.search.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemEmptyBinding
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.DiffUtilCallback

class SearchAdapter(
    private val onItemClick: (Vacancy) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>() {

    private val vacancy = ArrayList<Vacancy>()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            0
        } else {
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return when (viewType) {
            0 -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                SearchViewHolder.EmptyViewHolder(ItemEmptyBinding.inflate(layoutInflater, parent, false))
            }

            1 -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                SearchViewHolder.VacancyViewHolder(ItemVacancyBinding.inflate(layoutInflater, parent, false))
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        when (holder) {
            is SearchViewHolder.EmptyViewHolder -> { }
            is SearchViewHolder.VacancyViewHolder -> {
                holder.bind(vacancy[position - 1])
                holder.itemView.setOnClickListener {
                    onItemClick.invoke(vacancy[holder.adapterPosition - 1])
                }
            }
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
