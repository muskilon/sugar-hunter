package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.search.models.SearchFragmentState
import ru.practicum.android.diploma.ui.search.recyclerview.SearchAdapter
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private val searchAdapter by lazy {
        SearchAdapter { vacancy ->
            if (viewModel.clickDebounce()) {
                requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).isVisible = false

                findNavController().navigate(
                    R.id.action_searchFragment_to_vacancyFragment,
                    VacancyFragment.createArgs(vacancy.id)
                )
            }
        }.also {
            binding.searchRecyclerView.adapter = it
        }
    }

    companion object {
        private const val NULL_TEXT = ""
        private const val MOD_10 = 10
        private const val NUMBER_0 = 0
        private const val NUMBER_1 = 1
        private const val NUMBER_2 = 2
        private const val NUMBER_3 = 3
        private const val NUMBER_4 = 4
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

        //       Для тестирования!!! Можно удалять
//       Пример формирования options для @QueryMap
        val options: HashMap<String, String> = HashMap()
        options["text"] = "Java" // Как передавать поисковый запрос
        options["page"] = "2" // Как передавать номер нужной страницы
//        viewModel.searchVacancies(options)
//
//        viewModel.getVacancy("98561017")
//        viewModel.getVacancy("98899447")
//        viewModel.getIndustries()

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText(NULL_TEXT)
        }

        val searchEditText = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                binding.searchIcon.visibility = searchButtonVisibility(s)
                viewModel.searchDebounce(changedText = s?.toString() ?: "", options = options)
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
            is SearchFragmentState.Content -> {
                showContent(state.vacancy.items)
//                 Как получить количество найденных вакансий
                Log.d("Найдено вакансий: ", "Найдено вакансий: ${state.vacancy.found}")
//                 Как получить количество найденных вакансий
                Log.d("Всего страниц: ", "Всего страниц: ${state.vacancy.pages}")
//                 Как получить номер текущей страницы
                Log.d("Текущая страница: ", "Текущая страница: ${state.vacancy.page}")
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
        Log.d("errorMessage: ", errorMessage)
    }

    private fun showEmpty(emptyMessage: String) {
        with(binding) {
            vacancyCount.text = requireContext().getString(
                R.string.search_error_no_vacancies
            )
            vacancyCount.visibility = View.VISIBLE
            noInternet.visibility = View.VISIBLE
        }
        with(binding) {
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            somethingWrong.visibility = View.GONE
        }
        Log.d("emptyMessage: ", emptyMessage)
    }

    private fun showContent(vacancy: List<Vacancy>) {
        with(binding) {
            vacancyCount.text = "Найдено ${vacancy.size} ${countToString(vacancy.size)}"
            vacancyCount.visibility = View.VISIBLE
            searchRecyclerView.visibility = View.VISIBLE
        }
        with(binding) {
            placeholderSearch.visibility = View.GONE
            progressBar.visibility = View.GONE
            somethingWrong.visibility = View.GONE
            noInternet.visibility = View.GONE
        }
        searchAdapter.setData(vacancy)
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

    private fun countToString(count: Int): String {
        return when (count.mod(MOD_10)) {
            NUMBER_0 -> "вакансий"
            NUMBER_1 -> "вакансия"
            NUMBER_2, NUMBER_3, NUMBER_4 -> "вакансии"
            else -> "вакансий"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
