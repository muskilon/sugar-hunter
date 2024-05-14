package ru.practicum.android.diploma.ui.search.recyclerview

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class SearchViewHolder(
    private val binding: ItemVacancyBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val RADIUS_IN_DP = 12f
    }

    fun bind(model: Vacancy) {
        Glide.with(binding.vacancyLogo)
            .load(model.logos)
            .placeholder(R.drawable.vacancy_no_image_holder)
            .transform(RoundedCorners(dpToPx(itemView.context)))
            .into(binding.vacancyLogo)
        // Тут нужно будт заполнить остальные поля
    }

    private fun dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            RADIUS_IN_DP,
            context.resources.displayMetrics
        ).toInt()
    }

}
