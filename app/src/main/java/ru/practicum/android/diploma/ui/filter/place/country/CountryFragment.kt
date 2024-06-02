package ru.practicum.android.diploma.ui.filter.place.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.ui.Key
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceAdapter
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceState

class CountryFragment : Fragment() {
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CountryViewModel>()
    private val countryAdapter by lazy { getAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countryRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.countryRecycler.adapter = countryAdapter

        viewModel.getAreas()

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is ChoicePlaceState.Loading -> showLoading()
                is ChoicePlaceState.Error -> showError()
                is ChoicePlaceState.Empty -> showEmpty()
                is ChoicePlaceState.Content -> {
                    countryAdapter.setData(state.countries)
                    showContent()
                }
            }
        }
        binding.backButton.setOnClickListener { exit() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exit()
            }
        })
    }

    private fun showError() {
        binding.countryRecycler.isVisible = false
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = true
    }

    private fun showEmpty() {
        binding.countryRecycler.isVisible = false
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = false
    }

    private fun showContent() {
        binding.countryRecycler.isVisible = true
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = false
    }

    private fun showLoading() {
        binding.countryRecycler.isVisible = false
        binding.progressBar.isVisible = true
        binding.getListFailure.getListFailure.isVisible = false
    }

    private fun getAdapter() = ChoicePlaceAdapter { country ->
        setFragmentResult(
            Key.SET_AREA,
            bundleOf(
                Key.REGION_NAME to country.name,
                Key.REGION_ID to country.id,
                Key.COUNTRY_NAME to country.name,
                Key.COUNTRY_ID to country.id
            )
        )
        findNavController().popBackStack(R.id.choicePlaceFragment, false)
    }

    fun exit() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
