package ru.practicum.android.diploma.ui.favourite

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.databinding.FragmentFavouriteBinding
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavouriteViewHolder(view: View, private val binding: ItemVacancyBinding) : RecyclerView.ViewHolder(view) {

    @SuppressLint("ResourceType")
    fun bind(vacancy: VacancyDetails) {
        binding.vacancyName.text = vacancy.title
        binding.companyName.text = vacancy.employer.name
        binding.financeCount.text = getTextFromFinanceCount(vacancy.salary)

        Glide.with(itemView.context).load(vacancy.employer.logoUrls).placeholder(R.drawable.vacancy_no_image_holder)
            .transform(
                FitCenter(),
                RoundedCorners(
                    itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners)
                ),
            ).into(binding.vacancyLogo)
    }

    private fun getTextFromFinanceCount(salary: Salary?): String {
        return when {
            salary?.from != null && salary.to != null -> "от ${salary.from} до ${salary.to} ${salary.currency}"
            salary?.from != null -> "от ${salary.from} ${salary.currency}"
            salary?.to != null -> "до ${salary.to} ${salary.currency}"
            else -> "зарплата не указана"
        }
    }

}
