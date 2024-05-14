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
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val vacancyName: TextView = itemView.findViewById(R.id.vacancyName)
    private val companyName: TextView = itemView.findViewById(R.id.companyName)
    private val financeCount: TextView = itemView.findViewById(R.id.financeCount)
    private val vacancyLogo: ImageView = itemView.findViewById(R.id.vacancyLogo)

    @SuppressLint("ResourceType")
    fun bind(vacancy: Vacancy) {
        vacancyName.text = vacancy.title
        companyName.text = vacancy.employer

        val salary = vacancy.salary
        val salaryText = when {
            salary?.from != null && salary.to != null -> "от ${salary.from} до ${salary.to} ${salary.currency}"
            salary?.from != null -> "от ${salary.from} ${salary.currency}"
            salary?.to != null -> "до ${salary.to} ${salary.currency}"
            else -> "зарплата не указана"
        }

        financeCount.text = salaryText

        Glide.with(itemView.context).load(vacancy.logos).placeholder(R.drawable.vacancy_no_image_holder)
            .transform(
                FitCenter(),
                RoundedCorners(
                    itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners)
                ),
            ).into(vacancyLogo)
    }

}

