package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
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
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.ui.Key

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FilterViewModel>()
    private var filters: MutableMap<String, String> = mutableMapOf()
    private var oldFilters: MutableMap<String, String> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(Key.AREA_FILTERS) { _, bundle -> processBundle(bundle) }

        filters = viewModel.getFilters()
        oldFilters.putAll(filters)

        setStatements()

        binding.selectRegionActionButton.setOnClickListener { selectRegionActionButtonClickListener() }

        binding.salaryEdit.addTextChangedListener(getTextWatcher())

        binding.salaryClearButton.setOnClickListener {
            binding.salaryEdit.text.clear()
            salaryHeaderColor(null)
        }

        binding.salaryEdit.onFocusChangeListener = OnFocusChangeListener { _, isFocus ->
            salaryEditOnFocusChangeListener(isFocus)
        }

        binding.selectIndustryActionButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterFragment_to_choiceSphereFragment
            )
        }

        binding.buttonDecline.setOnClickListener {
            filters.clear()
            salaryHeaderColor(null)
            setStatements()
        }

        binding.buttonApply.setOnClickListener { buttonApplyListener() }

        binding.selectIndustryLayout.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_choiceSphereFragment)
        }
        binding.selectRegionLayout.setOnClickListener { selectRegionClick() }

        binding.salaryCheckBox.setOnClickListener {
            salaryCheckBoxProcessing()
        }

        binding.backButton.setOnClickListener { exit() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exit()
            }
        })
    }
    private fun processBundle(bundle: Bundle) {
        if (!bundle.isEmpty) {
            with(bundle) {
                getString(Key.REGION_NAME)?.let {
                    filters[Key.REGION_NAME] = it
                }
                getString(Key.REGION_ID)?.let {
                    filters[Key.AREA] = it
                }
                getString(Key.COUNTRY_NAME)?.let {
                    filters[Key.COUNTRY_NAME] = it
                } ?: filters.remove(Key.COUNTRY_NAME)
            }
            setStatements()
        }
    }
    private fun buttonApplyListener() {
        viewModel.updateFilters(filters)
        setFragmentResult(
            Key.REQUEST_KEY,
            bundleOf(Key.IS_APPLY_BUTTON to true)
        )
        findNavController().popBackStack(R.id.searchFragment, false)
    }
    private fun selectRegionActionButtonClickListener() {
        when (binding.selectRegionActionButton.tag) {
            Key.CLEAR -> {
                filters.remove(Key.REGION_NAME)
                filters.remove(Key.AREA)
                filters.remove(Key.COUNTRY_NAME)
                binding.selectRegionActionButton.setImageResource(R.drawable.leading_icon_filter)
                binding.selectRegionActionButton.tag = Key.ARROW
                setStatements()
            }

            Key.ARROW -> selectRegionClick()
        }
    }

    private fun salaryCheckBoxProcessing() {
        binding.salaryEdit.clearFocus()
        when (binding.salaryCheckBox.isChecked) {
            true -> filters[Key.ONLY_WITH_SALARY] = Key.TRUE
            false -> filters.remove(Key.ONLY_WITH_SALARY)
        }
        setStatements()
    }

    private fun salaryEditOnFocusChangeListener(isFocus: Boolean) {
        when (isFocus) {
            true -> salaryHeaderColor(true)
            false -> {
                if (filters[Key.SALARY].isNullOrEmpty()) {
                    salaryHeaderColor(null)
                } else {
                    salaryHeaderColor(false)
                }
            }
        }
    }

    private fun selectRegionClick() {
        setFragmentResult(
            Key.SET_AREA_FROM_FILTERS,
            bundleOf(
                Key.REGION_NAME to filters[Key.REGION_NAME],
                Key.REGION_ID to filters[Key.AREA],
                Key.COUNTRY_NAME to filters[Key.COUNTRY_NAME],
            )
        )
        findNavController().navigate(R.id.action_filterFragment_to_choicePlaceFragment)
    }

    private fun salaryHeaderColor(isFocus: Boolean?) {
        when (isFocus) {
            true -> {
                binding.salaryHeader.setTextColor(requireContext().getColorStateList(R.color.salary_header_focus))
            }

            null -> {
                binding.salaryHeader.setTextColor(requireContext().getColorStateList(R.color.salary_header_default))
            }

            false -> {
                binding.salaryHeader.setTextColor(requireContext().getColorStateList(R.color.salary_header_not_empty))
            }
        }
    }

    private fun setStatements() {
        binding.buttonApply.isVisible = oldFilters != filters
        if (filters.isNotEmpty()) {
            filters.keys.forEach { key ->
                when (key) {
                    Key.SALARY -> {
                        binding.salaryEdit.setText(filters[key])
                        binding.salaryClearButton.isVisible = true
                        salaryHeaderColor(false)
                    }

                    Key.ONLY_WITH_SALARY -> binding.salaryCheckBox.isChecked = true
                    Key.INDUSTRY -> Unit
                    Key.AREA -> renderArea()
                }
                binding.buttonDecline.isVisible = true
            }
        } else {
            binding.buttonDecline.isVisible = false
            binding.salaryCheckBox.isChecked = false
            binding.salaryEdit.text.clear()
            binding.selectedRegionsText.isVisible = false
        }
    }

    private fun renderArea() {
        if (filters[Key.COUNTRY_NAME].isNullOrEmpty()) {
            binding.selectedRegionsText.text = filters[Key.COUNTRY_NAME]
        } else {
            val st = "${filters[Key.COUNTRY_NAME]}, ${filters[Key.REGION_NAME]}"
            binding.selectedRegionsText.text = st
        }
        binding.selectRegionActionButton.setImageResource(R.drawable.clear_button)
        binding.selectRegionActionButton.tag = Key.CLEAR
        binding.selectedRegionsText.isVisible = true
    }

    private fun getTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty()) {
                filters.remove(Key.SALARY)
                binding.salaryClearButton.isVisible = false
                setStatements()
            } else {
                filters[Key.SALARY] = s.toString()
                binding.buttonApply.isVisible = oldFilters != filters
                binding.buttonDecline.isVisible = true
                binding.salaryClearButton.isVisible = true
            }
        }

        override fun afterTextChanged(s: Editable?) {
            // empty
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateFilters(filters)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun exit() {
        viewModel.updateFilters(filters)
        setFragmentResult(
            Key.REQUEST_KEY,
            bundleOf(Key.IS_APPLY_BUTTON to false)
        )
        findNavController().popBackStack(R.id.searchFragment, false)
    }
}
