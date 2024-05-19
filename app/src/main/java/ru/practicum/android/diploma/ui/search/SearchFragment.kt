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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private val searchAdapter by lazy { getAdapter() }
    private var searchText = EMPTY_TEXT

    companion object {
        private const val EMPTY_TEXT = ""
    }

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

        binding.searchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.searchRecyclerView.adapter = searchAdapter
        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText(EMPTY_TEXT)
        }
        binding.favoriteButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterFragment
            )
        }

        val searchEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                binding.searchIcon.visibility = searchButtonVisibility(s)
                searchText = s.toString()
                viewModel.searchDebounce(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.searchEditText.addTextChangedListener(searchEditText)

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.searchRecyclerView.addOnScrollListener(object :
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
                    if (pos >= itemsCount - 1 && !viewModel.isPageLoading) {
                        viewModel.isPageLoading = true // Это заглушка
                        binding.pageLoading.isVisible = true
                        viewModel.onLastItemReached()
                    }
                }
            }
        })

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.searchEditText.clearFocus()
                viewModel.searchDebounce(EMPTY_TEXT)
                if (searchText.isNotEmpty()) {
                    viewModel.searchVacancies(viewModel.getSearchRequest(searchText, null))
                }
            }
            false
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: SearchFragmentState) {
        when (state) {
            is SearchFragmentState.Start -> showStart()
            is SearchFragmentState.Content -> {
                totalFoundVacancies = state.vacancy.found
                showContent(state.vacancy)
            }
            is SearchFragmentState.Empty -> showEmpty(state.message)
            is SearchFragmentState.Error -> showError(state.errorMessage)
            is SearchFragmentState.Loading -> showLoading()
        }
    }

    private fun showStart() {
        with(binding) {
            placeholderSearch.visibility = View.VISIBLE
        }
        with(binding) {
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
        }
        with(binding) {
            placeholderSearch.visibility = View.GONE
            noInternet.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            vacancyCount.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
        }
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            noInternet.visibility = View.VISIBLE
        }
        with(binding) {
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
        }
        with(binding) {
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            noInternet.visibility = View.GONE
        }
        Log.d("emptyMessage: ", emptyMessage)
    }

    private fun showContent(vacancy: VacanciesResponse) {
        with(binding) {
            vacancyCount.text = App.getAppResources()?.getQuantityString(
                R.plurals.vacancy_plurals, totalFoundVacancies, totalFoundVacancies
            )
            vacancyCount.visibility = View.VISIBLE
            searchRecyclerView.visibility = View.VISIBLE
        }
        with(binding) {
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            noInternet.visibility = View.GONE
            pageLoading.isVisible = false
        }
        searchAdapter.setData(vacancy.items)
        viewModel.isPageLoading = false // Это заглушка
    }
    private fun getAdapter() =
        SearchAdapter { vacancy ->
            if (viewModel.clickDebounce()) {
                requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).isVisible = false

                findNavController().navigate(
                    R.id.action_searchFragment_to_vacancyFragment,
                    VacancyFragment.createArgs(vacancy.id)
                )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
