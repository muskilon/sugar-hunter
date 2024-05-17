package ru.practicum.android.diploma.ui.vacancy.presenter

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.vacancy.models.VacancyFragmentState
import ru.practicum.android.diploma.util.FormatUtilFunctions

class VacancyFragmentPresenter(val binding: FragmentVacancyBinding, val context: Context) {

    fun render(state: VacancyFragmentState) {
        when (state) {
            is VacancyFragmentState.Loading -> showLoading()
            is VacancyFragmentState.Content -> {
                showContent(state.vacancy)
                FormatUtilFunctions.downloadImage(
                    state.vacancy.logoUrls.logo240,
                    binding.vacancyImage,
                    context
                )
            }

            is VacancyFragmentState.Empty -> showEmpty()
            is VacancyFragmentState.Error -> showError()
            is VacancyFragmentState.Start -> showStart()
        }
    }

    private fun updateVisibility(vararg viewStates: Pair<View, Boolean>) {
        viewStates.forEach { it.first.isVisible = it.second }
    }

    private fun showLoading() {
        with(binding) {
            updateVisibility(
                progressBar to true,
            )
        }
    }

    private fun showEmpty() {
        with(binding) {
            updateVisibility(
                progressBar to false,
                errorPlaceholder to true
            )
        }
    }

    private fun showError() {
        with(binding) {
            updateVisibility(
                progressBar to false,
                errorPlaceholder to true
            )
        }
    }

    private fun showStart() {
        with(binding) {
            updateVisibility(
                scrollView to false
            )
        }
    }

    private fun showContent(vacancy: VacancyDetails) {
        with(binding) {
            scrollView.isVisible = true
            // Буду переделывать
            progressBar.isVisible = false

//            vacancyHeader.isVisible = true
            vacancyName.text = vacancy.title
            FormatUtilFunctions.showSalaryString(vacancy.salary, binding.vacancyCoast)

            vacancyCardName.text = vacancy.employer
            vacancyPlace.text = vacancy.city

            showExperience(vacancy)
            showSchedule(vacancy.schedule)

            aboutJobHeader.isVisible = true

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

    fun showContacts(contacts: Contacts?) {
        if (contacts == null) {
            binding.contactsGroup.isVisible = false
        } else {
            binding.contactsGroup.isVisible = true

            showContactName(contacts)

            showContactEmail(contacts)

            showContactPhone(contacts)
        }
    }

    fun showContactName(contacts: Contacts?) {
        if (contacts!!.name == null) {
            updateVisibility(
                binding.contactFaceHeader to false,
                binding.contactFaceResponse to false
            )
        } else {
            updateVisibility(
                binding.contactFaceHeader to true,
                binding.contactFaceResponse to true
            )
            binding.contactFaceResponse.text = contacts.name
        }
    }

    fun showContactEmail(contacts: Contacts?) {
        if (contacts!!.email == null) {
            updateVisibility(
                binding.contactEmailHeader to false,
                binding.contactEmailResponse to false
            )

        } else {
            updateVisibility(
                binding.contactEmailHeader to true,
                binding.contactEmailResponse to true
            )
            binding.contactEmailResponse.text = contacts.email
        }
    }

    fun showContactPhone(contacts: Contacts?) {
        if (contacts!!.phones == null) {
            updateVisibility(
                binding.contactPhoneHeader to false,
                binding.contactPhoneResponse to false
            )

        } else {
            updateVisibility(
                binding.contactPhoneHeader to true,
                binding.contactPhoneResponse to true
            )
            // мб переделать
            binding.contactPhoneResponse.text = contacts.phones!!.first().toString()
        }
    }
}
