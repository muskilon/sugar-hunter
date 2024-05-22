package ru.practicum.android.diploma.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.domain.models.EmailData

class ExternalNavigator(
    private val context: Context
) {
    fun shareLink(link: String) {
        val sendApp: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
            Intent.createChooser(this, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(sendApp)

    }

    fun openEmail(emailData: EmailData) {
        val sendFeedback = Intent(Intent.ACTION_SENDTO).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.address))
        }
        context.startActivity(sendFeedback)
    }

//    Нужен метод для звонка по телефону
}
