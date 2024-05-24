package ru.practicum.android.diploma.ui.filter.place.region

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.ui.Key
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceAdapter
import ru.practicum.android.diploma.ui.filter.place.ChoicePlaceState

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

        setFragmentResultListener(Key.CHOSEN_COUNTRY) { _, bundle ->
            bundle.getString(Key.CHOSEN_COUNTRY)?.let { viewModel.getAreas(it) } ?: viewModel.getAreas(String()) }

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.regionEditText.setOnEditorActionListener { _, actionId, _ ->
            regionEditActionListener(actionId)
            false
        }

        binding.regionRecycler.addOnScrollListener(getOnScrollListener(imm))

        binding.regionEditText.addTextChangedListener(getTextWatcher())

        binding.regionRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.regionRecycler.adapter = countryAdapter

        binding.clearIcon.setOnClickListener {
            binding.regionEditText.text.clear()
        }

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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exit()
                }
            }
        )
    }
    private fun getOnScrollListener(imm: InputMethodManager) = object :
        RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            imm.hideSoftInputFromWindow(binding.regionEditText.windowToken, 0)
        }
    }
    private fun regionEditActionListener(actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            binding.regionEditText.clearFocus()
        }
    }
    private fun getTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.clearIcon.isVisible = !s.isNullOrEmpty()
            binding.searchIcon.isVisible = s.isNullOrEmpty()
            viewModel.search(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            // empty
        }
    }

    private fun showError() {
        binding.regionRecycler.isVisible = false
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = true
        binding.noRegion.noRegion.isVisible = false
    }

    private fun showEmpty() {
        binding.regionRecycler.isVisible = false
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = true
        binding.noRegion.noRegion.isVisible = true
    }

    private fun showContent() {
        binding.regionRecycler.isVisible = true
        binding.progressBar.isVisible = false
        binding.getListFailure.getListFailure.isVisible = false
        binding.noRegion.noRegion.isVisible = false
    }

    private fun showLoading() {
        binding.regionRecycler.isVisible = false
        binding.progressBar.isVisible = true
        binding.getListFailure.getListFailure.isVisible = false
        binding.noRegion.noRegion.isVisible = false
    }

    private fun getAdapter() = ChoicePlaceAdapter { region ->
        setFragmentResult(
            Key.SET_AREA,
            bundleOf(
                Key.REGION_NAME to region.name,
                Key.REGION_ID to region.id,
                Key.COUNTRY_NAME to region.countryName,
                Key.COUNTRY_ID to region.parentId
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
