package ru.practicum.android.diploma.ui.root.filter.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectCountryBinding
import ru.practicum.android.diploma.presentation.filter.place.SelectCountryViewModel

class SelectCountryFragment : Fragment() {

    private var _binding: FragmentSelectCountryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SelectCountryViewModel>()

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

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getCountries()

        with(binding) {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            recyclerView.adapter = areaAdapter
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun render(state: AreaState) {
        when (state) {
            is AreaState.Content -> {
                showContent()
                areaAdapter.updateList(state.areas)
            }

            is AreaState.NoInternet -> {
                showNoInternet()
            }

            else -> {
                showError()
            }
        }
    }

    private fun showError() {
        binding.placeholderImage.setImageResource(R.drawable.placeholder_empty_industry_list)
        binding.placeholder.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showNoInternet() {
        binding.placeholderImage.setImageResource(R.drawable.placeholder_no_internet)
        binding.placeholder.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showContent() {
        binding.placeholder.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }
}
