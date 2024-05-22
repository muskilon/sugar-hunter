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
import androidx.room.util.convertByteToUUID
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
    private var areaName: String? = null
    private var areaId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("country") { _, bundle ->
            areaName = bundle.getString("areaName")
            areaId = bundle.getString("areaId")
            binding.selectedCountryText.text = areaName
            binding.selectedCountryText.isVisible = true
            binding.selectCountryActionButton.setImageResource(R.drawable.clear_button)
            Log.d("TAG", "$areaName $areaId")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
