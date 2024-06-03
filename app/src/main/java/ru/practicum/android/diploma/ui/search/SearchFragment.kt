package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.ui.Key
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState
import ru.practicum.android.diploma.ui.search.recyclerview.SearchAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var totalFoundVacancies = 0
    private var pages = 0
    private var currentPage = 0
    private val searchAdapter by lazy { getAdapter() }
    private var searchText: String? = null
    private var isClickAllowed = true
    private var isPageLoading = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(Key.REQUEST_KEY) { _, bundle ->
            if (bundle.getBoolean(Key.IS_APPLY_BUTTON)) {
                viewModel.repeatRequest(binding.searchEditText.text.toString(), false)
            }
        }
        val presenter = SearchFragmentPresenter(binding)

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (viewModel.isFiltersOn()) {
            binding.favoriteButton.setImageResource(R.drawable.search_filter_active)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.search_filter_inactive)
        }

        binding.searchEditText.addTextChangedListener(getTextWatcher(presenter))

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            searchEditActionListener(actionId)
            false
        }

        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.searchRecyclerView.adapter = searchAdapter

        binding.searchRecyclerView.addOnScrollListener(getOnScrollListener(imm))

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state, presenter)
        }

        binding.clearIcon.setOnClickListener { clearIconClickListener(presenter) }

        binding.favoriteButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterFragment
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { exit() }
        })
    }

    private fun clearIconClickListener(presenter: SearchFragmentPresenter) {
        binding.searchEditText.text.clear()
        emptySearchInputState(presenter)
    }
    private fun searchEditActionListener(actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            binding.searchEditText.clearFocus()
            viewModel.searchDebounce(String())
            searchText?.let {
                viewModel.searchVacancies(viewModel.getSearchRequest(it, null))
            } ?: {
                viewModel.searchVacancies(viewModel.getSearchRequest(String(), null))
            }
        }
    }
    private fun getOnScrollListener(imm: InputMethodManager) = object :
        RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                val pos = (binding.searchRecyclerView.layoutManager as LinearLayoutManager)
                    .findLastVisibleItemPosition()
                val itemsCount = searchAdapter.itemCount
                if (pos >= itemsCount - 1 && !isPageLoading && currentPage < pages - 1) {
                    isPageLoading = true
                    viewModel.repeatRequest(String(), true)
                    binding.pageLoading.isVisible = true
                }
            }
        }
    }

    private fun emptySearchInputState(presenter: SearchFragmentPresenter) {
        if (searchAdapter.itemCount != 0) {
            presenter.justShowContent(totalFoundVacancies)
            isPageLoading = false
        } else {
            presenter.showStart()
        }
    }
    private fun getTextWatcher(presenter: SearchFragmentPresenter) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.clearIcon.isVisible = !s.isNullOrEmpty()
            binding.searchIcon.isVisible = s.isNullOrEmpty()
            searchText = s.toString()
            if (searchText.isNullOrEmpty()) emptySearchInputState(presenter)
            searchText?.let { viewModel.searchDebounce(it) } ?: { viewModel.searchDebounce(String()) }
        }
        override fun afterTextChanged(s: Editable?) = Unit
    }
    private fun render(state: SearchFragmentState, presenter: SearchFragmentPresenter) {
        when (state) {
            is SearchFragmentState.Start -> {
                presenter.showStart()
            }
            is SearchFragmentState.Content -> {
                pages = state.vacancy.pages
                currentPage = state.vacancy.page
                totalFoundVacancies = state.vacancy.found
                presenter.showContent(
                    vacancy = state.vacancy,
                    searchAdapter = searchAdapter,
                    totalFoundVacancies = totalFoundVacancies
                )
                isPageLoading = false
            }
            is SearchFragmentState.Empty -> presenter.showEmpty(state.message)
            is SearchFragmentState.Error -> {
                if (state.isSearch) {
                    presenter.showError(state.errorMessage)
                } else {
                    binding.pageLoading.isVisible = false
                    isPageLoading = false
                    if (toastDebounce()) {
                        Toast.makeText(
                            requireContext(),
                            state.errorMessage,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
            is SearchFragmentState.Loading -> presenter.showLoading()
        }
    }
    private fun toastDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(Key.TOAST_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
    private fun getAdapter() =
        SearchAdapter { vacancy ->
            if (viewModel.clickDebounce()) {
                findNavController().navigate(
                    R.id.action_searchFragment_to_vacancyFragment,
                    VacancyFragment.createArgs(vacancy.id)
                )
            }
        }
    private fun exit() {
        if (toastDebounce()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.exit_message),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            requireActivity().finish()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
