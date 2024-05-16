package ru.practicum.android.diploma.util

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.search.recyclerview.SearchViewHolder
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment

object FormatUtilFunctions {
    fun formatLongNumber(salary: Long): String {
        val numberString = salary.toString()
        val result = StringBuilder()

        val length = numberString.length
        for (i in 0 until length) {
            result.append(numberString[i])

            if ((length - i - 1) % VacancyFragment.THREE_DIGITS_FOR_SPACE == 0 && i != length - 1) {
                result.append(' ')
            }
        }

        return result.toString()
    }

    fun getCurrency(currency: String): String {
        return when (currency) {
            "RUR", "RUB" -> "₽"
            "BYR" -> "BYR"
            "USD" -> "$"
            "EUR" -> "€"
            "KZT" -> "₸"
            "UAH" -> "₴"
            "AZN" -> "₼"
            "UZS" -> "Soʻm"
            "GEL" -> "₾"
            "KGT" -> "\u20C0"
            else -> currency
        }
    }

    fun downloadImage(url: String?, imageView: ImageView, context: Context) {
        if (url.isNullOrEmpty()) {
            return
        } else {
            Glide.with(imageView)
                .load(url)
                .placeholder(R.drawable.vacancy_no_image_holder)
                .transform(RoundedCorners(dpToPx(context)))
                .into(imageView)
        }
    }

    private fun dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            SearchViewHolder.VacancyViewHolder.RADIUS_IN_DP,
            context.resources.displayMetrics
        ).toInt()
    }
}
