package ru.practicum.android.diploma.ui.filter.place.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceAdapter
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceState
import ru.practicum.android.diploma.ui.filter.place.country.CountryViewModel

class RegionFragment : Fragment() {
    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<RegionViewModel>()
    private val countryAdapter by lazy { getAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.regionRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.regionRecycler.adapter = countryAdapter

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
    }

    private fun showError() {
        binding.regionRecycler.isVisible = false
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = true
    }

    private fun showEmpty() {
        binding.regionRecycler.isVisible = false
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = false
    }

    private fun showContent() {
        binding.regionRecycler.isVisible = true
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = false
    }

    private fun showLoading() {
        binding.regionRecycler.isVisible = false
        binding.progressBar.isVisible = true
        binding.getListFailure.getListFailure.isVisible = false
    }

    private fun getAdapter() = ChoicePlaceAdapter { region ->
        setFragmentResult("region", bundleOf("areaName" to region.name, "areaId" to region.id, "countryName" to region.countryName))
        findNavController().popBackStack(R.id.choicePlaceFragment, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
