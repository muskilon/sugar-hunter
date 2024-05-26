package ru.practicum.android.diploma.ui.vacancy.presenter

import android.text.Html
import android.text.util.Linkify
import android.util.Log
import androidx.core.view.isVisible
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.vacancy.models.VacancyFragmentState
import ru.practicum.android.diploma.util.FormatUtilFunctions

class VacancyFragmentPresenter(val binding: FragmentVacancyBinding) {
    val util = FormatUtilFunctions()

    fun render(state: VacancyFragmentState) {
        when (state) {
            is VacancyFragmentState.Loading -> showLoading()
            is VacancyFragmentState.Content -> {
                showContent(state.vacancy)
                util.downloadImage(
                    state.vacancy.logoUrls.logo240,
                    binding.vacancyImage
                )
            }

            is VacancyFragmentState.Empty -> showEmpty()
            is VacancyFragmentState.Error -> showError()
            is VacancyFragmentState.Start -> showStart()
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.isVisible = true
        }
    }

    private fun showEmpty() {
        with(binding) {
            progressBar.isVisible = false
            errorPlaceholder.isVisible = true
        }
    }

    private fun showError() {
        with(binding) {
            progressBar.isVisible = false
            errorPlaceholder.isVisible = true
        }
    }

    private fun showStart() {
        with(binding) {
            scrollView.isVisible = false
        }
    }

    private fun showContent(vacancy: VacancyDetails) {
        with(binding) {
            scrollView.isVisible = true
            progressBar.isVisible = false
            vacancyName.text = vacancy.title
            vacancyCardName.text = vacancy.employer
            showAddress(vacancy)
            util.showSalaryString(vacancy.salary, binding.vacancyCoast)
            showExperience(vacancy)
            showSchedule(vacancy.schedule)
            showDescription(vacancy)
            showKeySkills(vacancy)
            showContacts(vacancy.contacts)
        }
    }

    fun showExperience(vacancy: VacancyDetails) {
        if (vacancy.experience == null) {
            binding.aboutJobGroup.isVisible = false

        } else {
            binding.experienceResponse.text = vacancy.experience.name
            binding.aboutJobGroup.isVisible = true
        }
    }

    fun showSchedule(schedule: Schedule?) {
        if (schedule == null) {
            binding.requestListResponse.isVisible = false
        } else {
            binding.requestListResponse.isVisible = true
            binding.requestListResponse.text = schedule.name
        }
    }

    fun showDescription(vacancy: VacancyDetails) {
        binding.description.setText(Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_COMPACT))
    }

    fun showContacts(contacts: Contacts?) {
        if (contacts == null || contacts.phones.isNullOrEmpty() && contacts.email == null) {
            Log.d("con", contacts.toString())
            binding.contactsGroup.isVisible = false
        } else {
            Log.d("con", contacts.toString())
            binding.contactsGroup.isVisible = true
            showContactName(contacts)
            showContactEmail(contacts)
            showContactPhone(contacts)
            showComments(contacts)
        }
    }

    fun showContactName(contacts: Contacts?) {
        if (contacts!!.name == null) {
            binding.contactFaceHeader.isVisible = false
            binding.contactFaceResponse.isVisible = false
        } else {
            binding.contactFaceHeader.isVisible = true
            binding.contactFaceResponse.isVisible = true
            binding.contactFaceResponse.text = contacts.name
        }
    }

    fun showContactEmail(contacts: Contacts?) {
        if (contacts!!.email == null) {
            binding.contactEmailHeader.isVisible = false
            binding.contactEmailResponse.isVisible = false
        } else {
            binding.contactEmailHeader.isVisible = true
            binding.contactEmailResponse.isVisible = true
            binding.contactEmailResponse.text = contacts.email
        }
    }

    fun showContactPhone(contacts: Contacts?) {
        if (contacts!!.phones == null) {
            binding.contactPhoneHeader.isVisible = false
            binding.contactPhoneResponse.isVisible = false
        } else {
            binding.contactPhoneHeader.isVisible = true
            binding.contactPhoneResponse.isVisible = true
            val text = contacts.phones!!.joinToString(" ")
            binding.contactPhoneResponse.text = text
            Linkify.addLinks(binding.contactPhoneResponse, Linkify.PHONE_NUMBERS)
        }
    }

    fun showComments(contacts: Contacts?) {
        if (contacts!!.phones.isNullOrEmpty()) {
            binding.contactCommentsHeader.isVisible = false
            binding.contactContactResponse.isVisible = false
        } else {
            binding.contactCommentsHeader.isVisible = true
            binding.contactContactResponse.isVisible = true
            binding.contactContactResponse.text = contacts.phones?.first()?.comment
        }
    }

    fun showKeySkills(vacancy: VacancyDetails) {
        if (vacancy.keySkills.isNullOrEmpty()) {
            binding.keySkillsHeader.isVisible = false
            binding.keySkillsResponce.isVisible = false
        } else {
            binding.keySkillsHeader.isVisible = true
            binding.keySkillsResponce.isVisible = true
            val skillList: List<String> = vacancy.keySkills
            val sb = StringBuilder()

            for (skill in skillList) {
                sb.append("• ").append(skill).append("\n")
            }

            val result = sb.toString()
            binding.keySkillsResponce.text = result
        }
    }

    fun showAddress(vacancy: VacancyDetails) {
        if (vacancy.address == null) {
            binding.vacancyPlace.text = vacancy.city
            return
        }

        if (vacancy.address.city == null && vacancy.address.street == null && vacancy.address.building == null) {
            binding.vacancyPlace.text = vacancy.city
            return
        }

        var string = ""
        if (vacancy.address.city != null) {
            string = vacancy.address.city
        }
        if (vacancy.address.street != null) {
            string = string.plus(", ${vacancy.address.street}")
        }
        if (vacancy.address.building != null) {
            string = string.plus(", ${vacancy.address.building}")
        }
        binding.vacancyPlace.text = string

    }

}
