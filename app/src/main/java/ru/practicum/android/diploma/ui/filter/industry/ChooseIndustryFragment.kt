package ru.practicum.android.diploma.ui.filter.industry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChoiceSphereBinding
import ru.practicum.android.diploma.domain.models.Industries

class ChooseIndustryFragment : Fragment() {

    private var _binding: FragmentChoiceSphereBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChooseIndustryViewModel>()
    private val adapter by lazy { IndustryAdapter { industry -> saveIndustry(industry) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoiceSphereBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.industryListLiveData().observe(viewLifecycleOwner) {industriesList ->
            adapter.industryList = industriesList
            adapter.notifyDataSetChanged()
        }

        binding.industryRecycler.adapter = adapter
        binding.industryRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    fun saveIndustry(industry: Industries) {
        // сохр в шаред
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
