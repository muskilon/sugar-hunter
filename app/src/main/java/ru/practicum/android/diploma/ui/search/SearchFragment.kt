package ru.practicum.android.diploma.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var searchEditText: TextWatcher

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

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText("")
            closeKeyboard()
        }

        searchEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                binding.searchIcon.visibility = searchButtonVisibility(s)
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        searchEditText.let {
            binding.searchEditText.addTextChangedListener(it)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: SearchFragmentState) {
        when (state) {
            is SearchFragmentState.Start -> showStart()
            is SearchFragmentState.Content -> showContent(state.vacancy)
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
            somethingWrong.visibility = View.VISIBLE
            vacancyCount.text = requireContext().getString(
                R.string.search_error_no_vacancies
            )
            vacancyCount.visibility = View.VISIBLE
        }
        with(binding) {
            placeholderSearch.visibility = View.GONE
            noInternet.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
        }
    }

    private fun showEmpty(emptyMessage: String) {
        with(binding) {
            noInternet.visibility = View.VISIBLE
        }
        with(binding) {
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            vacancyCount.visibility = View.GONE
        }
    }

    private fun showContent(vacancy: List<Vacancy>) {
        with(binding) {
            vacancyCount.visibility = View.GONE
            searchRecyclerView.visibility = View.VISIBLE
        }
        with(binding) {
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            noInternet.visibility = View.GONE
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

    private fun closeKeyboard(){
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchEditText.let { binding.searchEditText.removeTextChangedListener(it) }
        _binding = null
    }
}
