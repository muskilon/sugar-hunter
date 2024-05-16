package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.ui.vacancy.presenter.VacancyFragmentPresenter

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel {
        parametersOf(vacancyId)
    }

    val vacancyId by lazy { requireArguments().getString(ARGS_VACANCY) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vacancyFragmentPresenter = VacancyFragmentPresenter(binding, requireContext())

        viewModel.getVacancyScreenStateLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer { state ->
            vacancyFragmentPresenter.render(state)
        })

        viewModel.checkInFavouritesLiveData()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { checkInFavourites ->
                if (checkInFavourites) {
                    binding.favoriteButton.setImageResource(R.drawable.favorite_active)
                } else {
                    binding.favoriteButton.setImageResource(R.drawable.favorite_inactive)
                }

                // нужен метод для сетонкликлистенера на favoriteButton (сейчас нет вакансии, чтобы реализовать)

            })
    }

    companion object {
        const val ARGS_VACANCY = "args_vacancy"
        const val THREE_DIGITS_FOR_SPACE = 3
        fun createArgs(id: String): Bundle =
            bundleOf(ARGS_VACANCY to id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
