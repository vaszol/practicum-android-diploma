package ru.practicum.android.diploma.ui.root.filter.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.databinding.FragmentSelectCountryBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.presentation.filter.place.SelectPlaceViewModel

class SelectCountryFragment : Fragment() {

    private var _binding: FragmentSelectCountryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<SelectPlaceViewModel>()

    private val areaAdapter by lazy {
        AreaAdapter {
            viewModel.setCountry(it)
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCountriesList()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when {
                state.error -> showError()
                state.noInternet -> showNoInternet()
            }
        }

        viewModel.areasToDisplay.observe(viewLifecycleOwner) { countries ->
            showContent(countries)
        }

        with(binding) {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            recyclerView.adapter = areaAdapter
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun showError() {
        binding.placeholder.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showNoInternet() {
        binding.placeholder.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showContent(countries: List<Area>) {
        binding.placeholder.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        areaAdapter.updateList(countries)
    }
}
