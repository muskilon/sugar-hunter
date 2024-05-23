package ru.practicum.android.diploma.ui.filter.place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoicePlaceBinding

class ChoicePlaceFragment : Fragment() {

    private var _binding: FragmentChoicePlaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChoicePlaceViewModel>()

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

        setFragmentResultListener("country") { _, bundle ->
            if (!bundle.isEmpty) viewModel.setArea(bundle)
        }
        setFragmentResultListener("region") { _, bundle ->
            if (!bundle.isEmpty) viewModel.setArea(bundle)
        }

        binding.selectCountryActionButton.setOnClickListener {
            when (binding.selectCountryActionButton.tag) {
                "clear" -> {
                    viewModel.clearArea()
                }

                "arrow" -> {
                    findNavController().navigate(
                        R.id.action_choicePlaceFragment_to_countryFragment
                    )
                }
            }
        }

        viewModel.getArea().observe(viewLifecycleOwner){
            render(it)
        }

        binding.selectCountryButtonGroup.setOnClickListener {
            findNavController().navigate(
                R.id.action_choicePlaceFragment_to_countryFragment
            )
        }

        binding.selectRegionButtonGroup.setOnClickListener {
            findNavController().navigate(
                R.id.action_choicePlaceFragment_to_regionFragment
            )
        }

    }

    private fun render(area: MutableMap<String, String>) {
        if (area.isEmpty()) {
            binding.selectedCountryText.text = null
            binding.selectedCountryText.isVisible = false
            binding.selectCountryActionButton.setImageResource(R.drawable.leading_icon_filter)
            binding.selectCountryActionButton.tag = "arrow"
            binding.buttonApply.isVisible = false
        } else {
            binding.selectedCountryText.text = area["areaName"]
            binding.selectedCountryText.isVisible = true
            binding.selectCountryActionButton.setImageResource(R.drawable.clear_button)
            binding.selectCountryActionButton.tag = "clear"
            binding.buttonApply.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
