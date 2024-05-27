package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import ru.practicum.android.diploma.util.FormatUtilFunctions

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FilterViewModel>()
    private var oldFilters: MutableMap<String, String> = mutableMapOf()

    private val formatUtil = FormatUtilFunctions()

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

        setFragmentResultListener(Key.AREA_FILTERS) { _, bundle -> viewModel.processBundle(bundle) }

        viewModel.getFilters().observe(viewLifecycleOwner) {
            setStatements(it)
        }

        initFilters()

        binding.selectRegionActionButton.setOnClickListener { selectRegionActionButtonClickListener() }

        binding.selectIndustryActionButton.setOnClickListener { selectIndustryActionButtonClickListener() }

        binding.salaryEdit.addTextChangedListener(getTextWatcher())

        binding.salaryClearButton.setOnClickListener { salaryClearIconListener() }

        binding.salaryEdit.onFocusChangeListener = OnFocusChangeListener { _, isFocus ->
            salaryEditOnFocusChangeListener(isFocus)
        }

        binding.selectIndustryActionButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterFragment_to_choiceSphereFragment
            )
        }

        binding.buttonDecline.setOnClickListener {
            viewModel.clearFilters()
            binding.salaryEdit.clearFocus()
            salaryHeaderColor(null)
        }

        binding.buttonApply.setOnClickListener { buttonApplyListener() }

        binding.selectIndustryLayout.setOnClickListener { selectIndustryClick() }

        binding.selectRegionLayout.setOnClickListener { selectRegionClick() }

        binding.salaryCheckBox.setOnClickListener {
            binding.salaryEdit.clearFocus()
            viewModel.salaryCheckBoxProcessing(binding.salaryCheckBox.isChecked)
        }

        binding.backButton.setOnClickListener { exit() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exit()
            }
        })
    }

    private fun buttonApplyListener() {
        viewModel.updateFiltersInStorage()
//        Log.d("BUTTON_APPLY_TAG", viewModel.getFiltersFromStorage().toString())
        setFragmentResult(
            Key.REQUEST_KEY,
            bundleOf(Key.IS_APPLY_BUTTON to true)
        )
        findNavController().popBackStack(R.id.searchFragment, false)
    }
    private fun selectRegionActionButtonClickListener() {
        when (binding.selectRegionActionButton.tag) {
            Key.CLEAR -> {
                viewModel.clearRegion()
            }

            Key.ARROW -> selectRegionClick()
        }
    }

    private fun selectIndustryActionButtonClickListener() {
        when (binding.selectRegionActionButton.tag) {
            //  TODO Отредактировать
            Key.CLEAR -> {
                binding.selectRegionActionButton.setImageResource(R.drawable.leading_icon_filter)
                binding.selectRegionActionButton.tag = Key.ARROW
            }

            Key.ARROW -> selectRegionClick()
        }
    }

    private fun salaryEditOnFocusChangeListener(isFocus: Boolean) {
        when (isFocus) {
            true -> salaryHeaderColor(true)
            false -> {
                if (binding.salaryEdit.text.isEmpty()) {
                    salaryHeaderColor(null)
                } else {
                    salaryHeaderColor(false)
                }
            }
        }
    }

    private fun salaryClearIconListener() {
        binding.salaryEdit.text.clear()
        viewModel.setSalary(null)
        if (binding.salaryEdit.isFocused) {
            salaryHeaderColor(true)
        } else {
            salaryHeaderColor(null)
        }
    }

    private fun selectRegionClick() {
        setFragmentResult(
            Key.SET_AREA,
            viewModel.getBundle()
        )
        findNavController().navigate(R.id.action_filterFragment_to_choicePlaceFragment)
    }

    private fun selectIndustryClick() {
        // TODO Сделать клон метода выше
        findNavController().navigate(R.id.action_filterFragment_to_choiceSphereFragment)

    }

    private fun salaryHeaderColor(isFocus: Boolean?) {
        with(binding.salaryHeader) {
            when (isFocus) {
                true -> setTextColor(requireContext().getColorStateList(R.color.salary_header_focus))
                null -> setTextColor(requireContext().getColorStateList(R.color.salary_header_default))
                false -> setTextColor(requireContext().getColorStateList(R.color.salary_header_not_empty))
            }
        }
    }

    private fun setStatements(filters: MutableMap<String, String>) {
        Log.d("SET_STATEMENTS_TAG", filters.toString())
        binding.buttonApply.isVisible = oldFilters != filters
        renderArea(filters)
        if (filters.isNotEmpty()) {
            filters.keys.forEach { key ->
                when (key) {
                    Key.ONLY_WITH_SALARY -> binding.salaryCheckBox.isChecked = true
                    Key.INDUSTRY -> Unit // TODO подключить renderIndustry
                }
                binding.buttonDecline.isVisible = true
            }
        } else {
            binding.buttonDecline.isVisible = false
            binding.salaryCheckBox.isChecked = false
            binding.salaryEdit.text.clear()
        }
    }

    private fun renderArea(filters: MutableMap<String, String>) {
        if (filters[Key.REGION_ID] != null) {
            formatUtil.formatSelectedFilterTextHeader(binding.selectRegionHeader)
            binding.selectRegionActionButton.setImageResource(R.drawable.clear_button)
            binding.selectRegionActionButton.tag = Key.CLEAR
            binding.selectedRegionsText.isVisible = true
            if (filters[Key.COUNTRY_ID] == filters[Key.REGION_ID]) {
                binding.selectedRegionsText.text = filters[Key.REGION_NAME]
            } else {
                val st = "${filters[Key.COUNTRY_NAME]}, ${filters[Key.REGION_NAME]}"
                binding.selectedRegionsText.text = st
            }
        } else {
            binding.selectedRegionsText.text = filters[Key.COUNTRY_NAME]
            formatUtil.formatUnselectedFilterTextHeader(binding.selectRegionHeader)
            binding.selectRegionActionButton.tag = Key.ARROW
            binding.selectedRegionsText.isVisible = false
            binding.selectRegionActionButton.setImageResource(R.drawable.leading_icon_filter)
        }
    }

    private fun renderIndustry() {
        TODO("Сделать метод для отрасли как метод выше")
    }

    private fun getTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.salaryClearButton.isVisible = !s.isNullOrEmpty()
            if (binding.salaryEdit.isFocused) salaryHeaderColor(true)
            viewModel.setSalary(s)
        }

        override fun afterTextChanged(s: Editable?) = Unit
    }

    private fun initFilters() {
        oldFilters.putAll(viewModel.getFiltersFromStorage())
        Log.d("OLD_FILTERS_TAG", oldFilters.toString())
        oldFilters[Key.SALARY]?.let {
            binding.salaryEdit.setText(it)
            viewModel.setSalary(it)
            binding.salaryClearButton.isVisible = true
            salaryHeaderColor(false)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateFiltersInStorage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun exit() {
        viewModel.updateFiltersInStorage()
//        Log.d("EXIT_TAG", viewModel.getFiltersFromStorage().toString())
        setFragmentResult(
            Key.REQUEST_KEY,
            bundleOf(Key.IS_APPLY_BUTTON to false)
        )
        findNavController().popBackStack(R.id.searchFragment, false)
    }
}
