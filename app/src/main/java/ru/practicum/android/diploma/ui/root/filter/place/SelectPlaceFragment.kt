package ru.practicum.android.diploma.ui.root.filter.place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceBinding
import ru.practicum.android.diploma.presentation.filter.place.SelectPlaceViewModel

class SelectPlaceFragment : Fragment() {

    private var _binding: FragmentSelectPlaceBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SelectPlaceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setState()

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        with(binding) {
            selectCountryButton.setOnClickListener {
                navigateToSelectCountry()
            }
            selectRegionButton.setOnClickListener {
                navigateToSelectRegion()
            }
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            deleteCountry.setOnClickListener {
                clearCountry()
            }
            deleteRegion.setOnClickListener {
                clearRegion()
            }
            selectButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun navigateToSelectCountry() {
        findNavController().navigate(R.id.action_selectPlaceFragment_to_selectCountryFragment)
    }

    private fun navigateToSelectRegion() {
        findNavController().navigate(R.id.action_selectPlaceFragment_to_selectRegionFragment)
    }

    private fun clearCountry() {
        Log.d("Dtest", "deleteCountry.setOnClickListener")
        viewModel.clearCountry()
        binding.inputCountry.text = EMPTY_TEXT
    }

    private fun clearRegion() {
        Log.d("Dtest", "deleteRegion.setOnClickListener")
        viewModel.clearRegion()
        binding.inputRegion.text = EMPTY_TEXT
    }

    private fun render(state: WorkPlaceState) {
        with(binding) {
            if (state.country != null) {
                countryTitle.isVisible = false
                subtitleCountry.isVisible = true
                inputCountry.text = state.country.name
                inputCountry.isVisible = true
                deleteCountry.isVisible = true
            } else {
                countryTitle.visibility = View.VISIBLE
                subtitleCountry.isVisible = false
                inputCountry.text = EMPTY_TEXT
                inputCountry.isVisible = false
                deleteCountry.isVisible = false
            }

            if (state.region != null) {
                region.isVisible = false
                subtitleRegion.isVisible = true
                inputRegion.text = state.region.name
                inputRegion.isVisible = true
                deleteRegion.isVisible = true
            } else {
                region.isVisible = true
                subtitleRegion.isVisible = false
                inputRegion.text = EMPTY_TEXT
                inputRegion.isVisible = false
                deleteRegion.isVisible = false
            }

            selectButton.isVisible = state.country != null || state.region != null
        }
    }

    companion object {
        private const val EMPTY_TEXT = ""
    }
}
