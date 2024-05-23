package ru.practicum.android.diploma.ui.filter.industry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChoiceIndustryBinding
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.IndustryState

class ChooseIndustryFragment : Fragment() {

    private var _binding: FragmentChoiceIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChooseIndustryViewModel>()
    private val adapter by lazy { IndustryAdapter { industry -> saveIndustry(industry) } }

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

        viewModel.checkStateLiveData().observe(viewLifecycleOwner) {state ->
            render(state)
        }

        binding.industryRecycler.adapter = adapter
        binding.industryRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

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
        // сохр в шаред
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
