package ru.practicum.android.diploma.util

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Salary

class FormatUtilFunctions {
    private fun formatLongNumber(salary: Long): String {
        val numberString = salary.toString()
        val result = StringBuilder()

        val length = numberString.length
        for (i in 0 until length) {
            result.append(numberString[i])

            if ((length - i - 1) % THREE_DIGITS_FOR_SPACE == 0 && i != length - 1) {
                result.append(' ')
            }
        }

        return result.toString()
    }

    private fun getCurrency(currency: String?): String {
        var string = String()
        if (currency != null) {
            string = when (currency) {
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
        return string
    }

    fun downloadImage(url: String?, imageView: ImageView) {
        if (url.isNullOrEmpty()) {
            return
        } else {
            Glide.with(imageView)
                .load(url)
                .placeholder(R.drawable.vacancy_no_image_holder)
                .transform(RoundedCorners(dpToPx(imageView.context)))
                .into(imageView)
        }
    }

    private fun dpToPx(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            ROUNDED_CORNERS_12PX,
            context.resources.displayMetrics
        ).toInt()
    }

    fun showSalaryString(salary: Salary?, textView: TextView) {
        val nullString = textView.context.getString(R.string.no_salary_string)
        if (salary == null) {
            textView.isVisible = true
            textView.text = nullString
            return
        } else {
            var salaryText = String()

            if (salary.from != null) {
                salaryText =
                    "от ${formatLongNumber(salary.from)} " +
                    "${getCurrency(salary.currency)} "
            }

            if (salary.to != null) {
                salaryText = salaryText.plus(
                    "до ${formatLongNumber(salary.to)} " +
                        "${getCurrency(salary.currency)} "
                )
            }

            if (salary.from == null && salary.to == null) {
                salaryText = nullString
            }

            textView.isVisible = true
            textView.text = salaryText
        }
    }

    fun formatSelectedFilterTextHeader(textView: TextView) {
        val typedValue = TypedValue()
        val theme = textView.context.theme
        theme.resolveAttribute(R.attr.prime_color_black, typedValue, true)
        val color = typedValue.data
        textView.setTextColor(ColorStateList.valueOf(color))
        textView.textSize = TEXT_SIZE_12SP
    }

    fun formatUnselectedFilterTextHeader(textView: TextView) {
        textView.setTextColor(textView.context.getColor(R.color.dark_gray))
        textView.textSize = TEXT_SIZE_16SP
    }

    companion object {
        const val ROUNDED_CORNERS_12PX = 12f
        const val THREE_DIGITS_FOR_SPACE = 3
        const val TEXT_SIZE_12SP = 12F
        const val TEXT_SIZE_16SP = 16f
    }
}
