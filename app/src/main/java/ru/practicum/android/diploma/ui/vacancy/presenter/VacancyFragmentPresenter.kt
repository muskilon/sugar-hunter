package ru.practicum.android.diploma.ui.vacancy.presenter

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import ru.practicum.android.diploma.data.dto.Salary
import ru.practicum.android.diploma.data.network.responses.Contacts
import ru.practicum.android.diploma.data.network.responses.Schedule
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
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
                    state.vacancy.employer.logoUrls?.logo240,
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
                vacancyHeader to false,
                employerCard to false,
                aboutJobGroup to false,
                aboutJobHeader to false,
                responsibilitiesGroup to false,
                requirementsGroup to false,
                conditionsGroup to false,
                skillsGroup to false,
                contactsGroup to false,
                conditionsGroup to false
            )
        }
    }

    private fun showEmpty() {
        with(binding) {
            updateVisibility(
                progressBar to false,
                vacancyHeader to false,
                employerCard to false,
                aboutJobGroup to false,
                aboutJobHeader to false,
                responsibilitiesGroup to false,
                requirementsGroup to false,
                conditionsGroup to false,
                skillsGroup to false,
                contactsGroup to false,
                errorPlaceholder to true
            )
        }
    }

    private fun showError() {
        with(binding) {
            updateVisibility(
                progressBar to false,
                vacancyHeader to false,
                employerCard to false,
                aboutJobGroup to false,
                aboutJobHeader to false,
                responsibilitiesGroup to false,
                requirementsGroup to false,
                conditionsGroup to false,
                skillsGroup to false,
                contactsGroup to false,
                errorPlaceholder to true
            )
        }
    }

    private fun showStart() {
        with(binding) {
            updateVisibility(
                progressBar to false,
                vacancyHeader to false,
                employerCard to false,
                aboutJobGroup to false,
                aboutJobHeader to false,
                responsibilitiesGroup to false,
                requirementsGroup to false,
                conditionsGroup to false,
                skillsGroup to false,
                contactsGroup to false,
                errorPlaceholder to false
            )
        }
    }

    private fun showContent(vacancy: VacancyDetails) {
        with(binding) {
            progressBar.isVisible = false
            vacancyHeader.isVisible = true
            vacancyName.text = vacancy.title
            showSalaryString(vacancy.salary)
            employerCard.isVisible = true
            vacancyCardName.text = vacancy.employer.name
            vacancyPlace.text = vacancy.area.name
            aboutJobGroup.isVisible = true
            showExperience(vacancy)
            showSchedule(vacancy.schedule)
            vacancy.employment
            aboutJobHeader.isVisible = true
            responsibilitiesGroup.isVisible = true
            requirementsGroup.isVisible = true
            conditionsGroup.isVisible = true
            skillsGroup.isVisible = true
            showContacts(vacancy.contacts)

        }
    }

    fun showSalaryString(salary: Salary?) {
        if (salary == null) {
            binding.vacancyCoast.isVisible = false
            Log.d("return", "true")
        } else {
            var salaryText: String = ""

            if (salary.from != null) {
                salaryText =
                    "от ${FormatUtilFunctions.formatLongNumber(salary.from)} " +
                    "${FormatUtilFunctions.getCurrency(salary.currency)} "
            }

            if (salary.to != null) {
                salaryText = salaryText.plus(
                    "до ${FormatUtilFunctions.formatLongNumber(salary.to)} ${
                        FormatUtilFunctions.getCurrency(salary.currency)
                    }"
                )
            }

            binding.vacancyCoast.isVisible = true
            binding.vacancyCoast.text = salaryText
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
