package ru.practicum.android.diploma.ui.filter.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoicePlaceBinding
import ru.practicum.android.diploma.ui.Key

class ChoicePlaceFragment : Fragment() {

    private var _binding: FragmentChoicePlaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChoicePlaceViewModel>()
    private var chosenCountry = String()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoicePlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(Key.SET_AREA) { _, bundle ->
            if (!bundle.isEmpty) viewModel.setArea(bundle)
        }
        setFragmentResultListener(Key.SET_AREA_FROM_FILTERS) { _, bundle ->
            if (!bundle.isEmpty) viewModel.setAreaFromFilters(bundle)
        }

        binding.buttonApply.setOnClickListener {
            viewModel.savePlace()
            setFragmentResult(Key.AREA_FILTERS, viewModel.savePlace())
            findNavController().popBackStack(R.id.filterFragment, false)
        }

        binding.selectCountryActionButton.setOnClickListener { selectCountryActionButtonClickListener() }

        binding.selectRegionActionButton.setOnClickListener {
            selectRegionActionButtonClickListener()
        }

        viewModel.getArea().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.selectCountryButtonGroup.setOnClickListener {
            findNavController().navigate(R.id.action_choicePlaceFragment_to_countryFragment)
        }

        binding.selectRegionButtonGroup.setOnClickListener {
            setFragmentResult(Key.CHOSEN_COUNTRY, bundleOf(Key.CHOSEN_COUNTRY to chosenCountry))
            findNavController().navigate(R.id.action_choicePlaceFragment_to_regionFragment)
        }

        binding.backButton.setOnClickListener { exit() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exit()
            }
        })

    }

    private fun selectCountryActionButtonClickListener() {
        when (binding.selectCountryActionButton.tag) {
            Key.CLEAR -> viewModel.clearCountry()
            Key.ARROW -> findNavController().navigate(R.id.action_choicePlaceFragment_to_countryFragment)
        }
    }

    private fun selectRegionActionButtonClickListener() {
        when (binding.selectRegionActionButton.tag) {
            Key.CLEAR -> viewModel.clearRegion()
            Key.ARROW -> {
                setFragmentResult(Key.CHOSEN_COUNTRY, bundleOf(Key.CHOSEN_COUNTRY to chosenCountry))
                findNavController().navigate(R.id.action_choicePlaceFragment_to_regionFragment)
            }
        }
    }

    private fun render(area: MutableMap<String, String>) {
        with(binding) {
            if (area[Key.REGION_NAME].isNullOrEmpty()) {
                selectedRegionText.text = null
                selectedRegionText.isVisible = false
                selectRegionActionButton.setImageResource(R.drawable.leading_icon_filter)
                selectRegionActionButton.tag = Key.ARROW
            } else {
                selectedRegionText.text = area[Key.REGION_NAME]
                selectedRegionText.isVisible = true
                selectRegionActionButton.setImageResource(R.drawable.clear_button)
                selectRegionActionButton.tag = Key.CLEAR
            }
            if (area[Key.COUNTRY_NAME].isNullOrEmpty()) {
                selectedCountryText.text = null
                selectedCountryText.isVisible = false
                selectCountryActionButton.setImageResource(R.drawable.leading_icon_filter)
                selectCountryActionButton.tag = Key.ARROW
                chosenCountry = String()
            } else {
                selectedCountryText.text = area[Key.COUNTRY_NAME]
                selectedCountryText.isVisible = true
                selectCountryActionButton.setImageResource(R.drawable.clear_button)
                selectCountryActionButton.tag = Key.CLEAR
                chosenCountry = area[Key.COUNTRY_NAME]!!
            }
        }
        binding.buttonApply.isVisible = area.isNotEmpty()
    }

    fun exit() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
