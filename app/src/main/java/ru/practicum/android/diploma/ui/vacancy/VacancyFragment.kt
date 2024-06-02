package ru.practicum.android.diploma.ui.vacancy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.ui.vacancy.models.VacancyFragmentState
import ru.practicum.android.diploma.ui.vacancy.presenter.VacancyFragmentPresenter

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel {
        parametersOf(vacancyId)
    }

    private val vacancyId by lazy { requireArguments().getString(ARGS_VACANCY) }

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

        val vacancyFragmentPresenter = VacancyFragmentPresenter(binding)

        viewModel.getVacancyScreenStateLiveData().observe(viewLifecycleOwner) { state ->
            vacancyFragmentPresenter.render(state)

            if (state is VacancyFragmentState.Content) {
                binding.shareButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, state.vacancy.url)
                    startActivity(Intent.createChooser(intent, "Поделиться ссылкой через:"))
                }

            } else {
                binding.shareButton.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Загрузка не удалась, отправлять нечего",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        binding.favoriteButton.setOnClickListener {
            lifecycleScope.launch { viewModel.likeOrDislikeButton() }
        }

        viewModel.checkInFavouritesLiveData()
            .observe(viewLifecycleOwner) { checkInFavourites ->
                if (checkInFavourites) {
                    binding.favoriteButton.setImageResource(R.drawable.favorite_active)
                } else {
                    binding.favoriteButton.setImageResource(R.drawable.favorite_inactive)
                }
            }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        const val ARGS_VACANCY = "args_vacancy"
        fun createArgs(id: String): Bundle =
            bundleOf(ARGS_VACANCY to id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
