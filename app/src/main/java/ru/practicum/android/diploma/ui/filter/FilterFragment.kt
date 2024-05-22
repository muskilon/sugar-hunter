package ru.practicum.android.diploma.ui.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding

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

        filters = viewModel.getFilters()
        oldFilters.putAll(filters)

        setStatements()

        binding.salaryEdit.addTextChangedListener(getTextWatcher())

        binding.salaryClearButton.setOnClickListener {
            binding.salaryEdit.text.clear()
        }

        binding.selectIndustryActionButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_choicePlaceFragment_to_regionFragment
            )
        }

        binding.buttonDecline.setOnClickListener {
            filters.clear()
            setStatements()
        }

        binding.buttonApply.setOnClickListener {
            viewModel.updateFilters(filters)
            setFragmentResult("requestKey", bundleOf("isApplyButton" to true))
            findNavController().popBackStack(R.id.searchFragment, false)
        }

//        Для тестирования!

        binding.selectIndustryLayout.setOnClickListener { test(INDUSTRY, "7") }
        binding.selectRegionLayout.setOnClickListener { test(AREA, "1") }

//        Для тестирования!

        binding.salaryCheckBox.setOnClickListener {
            binding.salaryEdit.clearFocus()
            when (binding.salaryCheckBox.isChecked) {
                true -> filters[ONLY_WITH_SALARY] = "true"
                false -> filters.remove(ONLY_WITH_SALARY)
            }
            setStatements()
        }

        binding.backButton.setOnClickListener { exit() }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exit()
                }
            }
        )
    }
    private fun test(key: String, value: String) { // Для тестирования!!
        if (filters.contains(key)) {
            filters.remove(key)
        } else {
            filters[key] = value
        }
        setStatements()
    }
    private fun setStatements() {
        binding.buttonApply.isVisible = oldFilters != filters
        if (filters.isNotEmpty()) {
            filters.keys.forEach { key ->
                when (key) {
                    SALARY -> {
                        binding.salaryEdit.setText(filters[key])
                        binding.salaryClearButton.isVisible = true
                    }
                    ONLY_WITH_SALARY -> binding.salaryCheckBox.isChecked = true
                    INDUSTRY -> Unit
                    AREA -> Unit
                }
                binding.buttonDecline.isVisible = true
            }
        } else {
            binding.buttonDecline.isVisible = false
            binding.salaryCheckBox.isChecked = false
            binding.salaryEdit.text.clear()
        }
    }

    private fun getTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty()) {
                filters.remove(SALARY)
                binding.salaryClearButton.isVisible = false
                setStatements()
            } else {
                filters[SALARY] = s.toString()
                binding.buttonApply.isVisible = oldFilters != filters
                binding.buttonDecline.isVisible = true
                binding.salaryClearButton.isVisible = true
            }
        }
        override fun afterTextChanged(s: Editable?) {
            // empty
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun exit() {
        viewModel.updateFilters(filters)
        setFragmentResult("requestKey", bundleOf("isApplyButton" to false))
        findNavController().popBackStack(R.id.searchFragment, false)
    }

    companion object {
        private const val AREA = "area"
        private const val INDUSTRY = "industry"
        private const val SALARY = "salary"
        private const val ONLY_WITH_SALARY = "only_with_salary"
    }
}
