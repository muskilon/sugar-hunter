package ru.practicum.android.diploma.ui.favourite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavouriteBinding
import ru.practicum.android.diploma.domain.models.FavouritesState
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import java.lang.ref.WeakReference

class FavouriteFragment : Fragment() {

    private val context by lazy { WeakReference(requireContext()) }
    private val adapter by lazy { FavouriteAdapter({ vacancy -> goToVacancy(vacancy) }, context) }
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavouriteViewModel>()
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.checkStateLiveData().observe(viewLifecycleOwner) { favouriteState ->
            render(favouriteState)
        }

        binding.favoriteRecyclerView.adapter = adapter
        binding.favoriteRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    companion object {
        fun newInstance(): Fragment = FavouriteFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun goToVacancy(vacancy: VacancyDetails) {
        lifecycleScope.launch {
            if (clickDebounce()) {
                val bundle = Bundle().apply {
                    putString(VacancyFragment.ARGS_VACANCY, vacancy.id)
                }
                findNavController().navigate(
                    R.id.action_favouriteFragment_to_vacancyFragment,
                    bundle
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(state: FavouritesState) {
        when (state) {
            is FavouritesState.Error -> showError()
            is FavouritesState.Empty -> showEmpty()
            is FavouritesState.Content -> {
                showContent()
                adapter.favoriteDetailsList = state.favourites
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun showError() {
        binding.favoriteNetworkErrorHolder.isVisible = true
        binding.favoriteEmptyListHolder.isVisible = false
        binding.favoriteRecyclerView.isVisible = false
    }

    private fun showEmpty() {
        binding.favoriteNetworkErrorHolder.isVisible = false
        binding.favoriteEmptyListHolder.isVisible = true
        binding.favoriteRecyclerView.isVisible = false
    }

    private fun showContent() {
        binding.favoriteNetworkErrorHolder.isVisible = false
        binding.favoriteEmptyListHolder.isVisible = false
        binding.favoriteRecyclerView.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.readFavoriteList()
            viewModel.updateState()
        }

    }
}
