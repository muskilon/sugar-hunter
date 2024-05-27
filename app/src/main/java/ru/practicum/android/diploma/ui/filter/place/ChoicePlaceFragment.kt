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
import ru.practicum.android.diploma.util.FormatUtilFunctions

class ChoicePlaceFragment : Fragment() {

    private var _binding: FragmentChoicePlaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChoicePlaceViewModel>()
    private var chosenCountry = String()

    private val formatUtil = FormatUtilFunctions()

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

        binding.buttonApply.setOnClickListener {
            viewModel.savePlace()
            setFragmentResult(Key.AREA_FILTERS, viewModel.savePlace())
            findNavController().popBackStack(R.id.filterFragment, false)
        }

        binding.selectCountryActionButton.setOnClickListener { selectCountryActionButtonClickListener() }

        binding.selectRegionActionButton.setOnClickListener { selectRegionActionButtonClickListener() }

        viewModel.getArea().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                renderContent(it)
            } else {
                renderEmpty()
            }
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
            Key.CLEAR -> { viewModel.clearCountry() }
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

    private fun renderEmpty() {
        with(binding) {
            selectedRegionText.text = null
            selectedRegionText.isVisible = false
            selectRegionActionButton.setImageResource(R.drawable.leading_icon_filter)
            selectRegionActionButton.tag = Key.ARROW

            selectedCountryText.text = null
            selectedCountryText.isVisible = false
            selectCountryActionButton.setImageResource(R.drawable.leading_icon_filter)
            selectCountryActionButton.tag = Key.ARROW

            formatUtil.formatUnselectedFilterTextHeader(selectRegionHeader)
            formatUtil.formatUnselectedFilterTextHeader(selectCountryHeader)
            chosenCountry = String()
            buttonApply.isVisible = false
        }
    }

    private fun renderContent(area: MutableMap<String, String>) {
        with(binding) {
            if (area[Key.COUNTRY_ID] == area[Key.REGION_ID]) {
                selectedRegionText.text = null
                selectedRegionText.isVisible = false
                selectRegionActionButton.setImageResource(R.drawable.leading_icon_filter)
                selectRegionActionButton.tag = Key.ARROW

                selectedCountryText.text = area[Key.COUNTRY_NAME]
                selectedCountryText.isVisible = true
                selectCountryActionButton.setImageResource(R.drawable.clear_button)
                selectCountryActionButton.tag = Key.CLEAR

                area[Key.COUNTRY_NAME]?.let { chosenCountry = it }
                formatUtil.formatSelectedFilterTextHeader(selectCountryHeader)
                formatUtil.formatUnselectedFilterTextHeader(selectRegionHeader)
            } else {
                selectedRegionText.text = area[Key.REGION_NAME]
                selectedRegionText.isVisible = true
                selectRegionActionButton.setImageResource(R.drawable.clear_button)
                selectRegionActionButton.tag = Key.CLEAR

                selectedCountryText.text = area[Key.COUNTRY_NAME]
                selectedCountryText.isVisible = true
                selectCountryActionButton.setImageResource(R.drawable.clear_button)
                selectCountryActionButton.tag = Key.CLEAR

                area[Key.COUNTRY_NAME]?.let { chosenCountry = it }
                formatUtil.formatSelectedFilterTextHeader(selectCountryHeader)
                formatUtil.formatSelectedFilterTextHeader(selectRegionHeader)
            }
        }
        binding.buttonApply.isVisible = true
    }

    fun exit() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
