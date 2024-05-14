package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<VacancyViewModel>()

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

        viewModel.checkInFavouritesLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                binding.favoriteButton.setImageResource(R.drawable.favorite_active)
            } else {
                binding.favoriteButton.setImageResource(R.drawable.favorite_inactive)
            }

            // нужен метод для сетонкликлистенера на favoriteButton (сейчас нет вакансии, чтобы реализовать)

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
