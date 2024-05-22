package ru.practicum.android.diploma.ui.favourite

import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.FormatUtilFunctions
import java.lang.ref.WeakReference

class FavouriteViewHolder(private val binding: ItemVacancyBinding, private val context: WeakReference<Context>) :
    RecyclerView.ViewHolder(binding.root) {

    val utilFunctions = FormatUtilFunctions()

    companion object {
        const val RADIUS_IN_DP = 12f
    }
    fun bind(vacancy: VacancyDetails) {
        binding.vacancyName.text = vacancy.title
        binding.companyName.text = vacancy.employer
        utilFunctions.showSalaryString(vacancy.salary, binding.financeCount)

        fun dpToPx(context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                RADIUS_IN_DP,
                context.resources.displayMetrics
            ).toInt()
        }

        Log.d("url", vacancy.logoUrls.logo240.toString())
        Glide.with(binding.vacancyLogo)
            .load(vacancy.logoUrls.logo240)
            .placeholder(R.drawable.vacancy_no_image_holder)
            .transform(RoundedCorners(dpToPx(itemView.context)))
            .into(binding.vacancyLogo)
    }
}
