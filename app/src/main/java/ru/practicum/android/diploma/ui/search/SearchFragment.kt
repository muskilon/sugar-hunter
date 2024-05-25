package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.app.App
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.VacanciesResponse
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

        setFragmentResultListener("requestKey") { _, bundle ->
            if (bundle.getBoolean("isApplyButton") && !searchText.isNullOrEmpty()) {
                viewModel.repeatRequest(false)
            }
        }

        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (viewModel.isFiltersOn()) {
            binding.favoriteButton.setImageResource(R.drawable.search_filter_active)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.search_filter_inactive)
        }

        binding.searchEditText.addTextChangedListener(getTextWatcher())

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            searchEditActionListener(actionId)
            false
        }

        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.searchRecyclerView.adapter = searchAdapter

        binding.searchRecyclerView.addOnScrollListener(getOnScrollListener(imm))

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.text.clear()
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.favoriteButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterFragment
            )
        }
    }

    private fun searchEditActionListener(actionId: Int) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            binding.searchEditText.clearFocus()
            if (!searchText.isNullOrEmpty()) {
                viewModel.searchVacancies(viewModel.getSearchRequest(searchText!!, null))
                viewModel.searchDebounce(String())
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
                    viewModel.repeatRequest(true)
                    binding.pageLoading.isVisible = true
                }
            }
        }
    }

    private fun getTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.clearIcon.isVisible = !s.isNullOrEmpty()
            binding.searchIcon.isVisible = s.isNullOrEmpty()
            searchText = s.toString()
            searchText?.let { viewModel.searchDebounce(it) }
        }

        override fun afterTextChanged(s: Editable?) {
            // empty
        }
    }

    private fun render(state: SearchFragmentState) {
        when (state) {
            is SearchFragmentState.Start -> showStart()
            is SearchFragmentState.Content -> {
                pages = state.vacancy.pages
                currentPage = state.vacancy.page
                totalFoundVacancies = state.vacancy.found
                showContent(state.vacancy)
            }
            is SearchFragmentState.Empty -> showEmpty(state.message)
            is SearchFragmentState.Error -> {
                if (state.isSearch) {
                    showError(state.errorMessage)
                } else {
                    binding.pageLoading.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        state.errorMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            is SearchFragmentState.Loading -> showLoading()
        }
    }

    private fun showStart() {
        with(binding) {
            placeholderSearch.visibility = View.VISIBLE
            noInternet.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            progressBar.visibility = View.GONE
            vacancyCount.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            placeholderSearch.visibility = View.GONE
            noInternet.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            vacancyCount.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            searchRecyclerView.removeAllViewsInLayout() // очистка RV, что бы не моргали предыдущие результаты поиска
        }
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            noInternet.visibility = View.VISIBLE
            placeholderSearch.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            vacancyCount.visibility = View.GONE
        }
        Log.d("errorMessage: ", errorMessage)
    }

    private fun showEmpty(emptyMessage: String) {
        with(binding) {
            vacancyCount.text = requireContext().getString(
                R.string.search_error_no_vacancies
            )
            vacancyCount.visibility = View.VISIBLE
            somethingWrong.visibility = View.VISIBLE
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            noInternet.visibility = View.GONE
        }
        Log.d("emptyMessage: ", emptyMessage)
    }

    private fun showContent(vacancy: VacanciesResponse) {
        searchAdapter.setData(vacancy.items)
        isPageLoading = false
        with(binding) {
            vacancyCount.text = App.getAppResources()?.getQuantityString(
                R.plurals.vacancy_plurals, totalFoundVacancies, totalFoundVacancies
            )
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            noInternet.visibility = View.GONE
            vacancyCount.visibility = View.VISIBLE
            searchRecyclerView.visibility = View.VISIBLE
            pageLoading.isVisible = false
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
