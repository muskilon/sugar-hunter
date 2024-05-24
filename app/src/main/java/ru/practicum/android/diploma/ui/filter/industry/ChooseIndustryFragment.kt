package ru.practicum.android.diploma.ui.filter.industry

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoiceIndustryBinding
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.IndustryState

class ChooseIndustryFragment : Fragment() {

    private var _binding: FragmentChoiceIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChooseIndustryViewModel>()
    private val adapter by lazy { IndustryAdapter { industry -> saveIndustry(industry) } }
    private var searchText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoiceIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkStateLiveData().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        binding.industryRecycler.adapter = adapter
        binding.industryRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.industryEditText.addTextChangedListener(getTextWatcher())

        binding.industryEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.industryEditText.clearFocus()
                if (!searchText.isNullOrEmpty()) {
                    viewModel.searchDebounce(String())
                }
            }
            false
        }

        binding.clearIcon.setOnClickListener {
            binding.industryEditText.text.clear()
        }

        binding.buttonApply.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun getTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.clearIcon.visibility = clearButtonVisibility(s)
            binding.searchIcon.visibility = searchButtonVisibility(s)
            searchText = s.toString()
            searchText?.let { viewModel.searchDebounce(it) }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(state: IndustryState) {
        when (state) {
            is IndustryState.ConnectionError -> showNoInternet()
            is IndustryState.NotFound -> showNotFound()
            is IndustryState.Loading -> showProgressBar()
            is IndustryState.Content -> {
                showContent()
                adapter.industryList = state.industriesList
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchButtonVisibility(s: CharSequence?): Int {
        return if (!s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun showNoInternet() {
        binding.notFoundPlaceholder.isVisible = false
        binding.noInternet.isVisible = true
        binding.progressBar.isVisible = false
        binding.industryRecycler.isVisible = false
    }

    private fun showNotFound() {
        binding.noInternet.isVisible = false
        binding.progressBar.isVisible = false
        binding.notFoundPlaceholder.isVisible = true
        binding.industryRecycler.isVisible = false
    }

    private fun showProgressBar() {
        binding.noInternet.isVisible = false
        binding.notFoundPlaceholder.isVisible = false
        binding.progressBar.isVisible = true
        binding.industryRecycler.isVisible = false
    }

    private fun showContent() {
        binding.noInternet.isVisible = false
        binding.notFoundPlaceholder.isVisible = false
        binding.progressBar.isVisible = false
        binding.industryRecycler.isVisible = true
    }

    private fun saveIndustry(industry: Industries) {
        binding.buttonApply.isVisible = true
        setFragmentResult(
            "setArea",
            bundleOf(
                "industryName" to industry.name,
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
