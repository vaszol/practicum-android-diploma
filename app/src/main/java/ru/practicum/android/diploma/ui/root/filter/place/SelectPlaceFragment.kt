package ru.practicum.android.diploma.ui.root.filter.place

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceBinding
import ru.practicum.android.diploma.presentation.filter.place.PlaceState
import ru.practicum.android.diploma.presentation.filter.place.SelectPlaceViewModel
import ru.practicum.android.diploma.presentation.filter.place.WorkPlaceState
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.APPLY_PLACE_KEY
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.PLACE_REQUEST_KEY
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.SELECTED_PLACE_KEY

class SelectPlaceFragment : Fragment() {
    private var _binding: FragmentSelectPlaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModel<SelectPlaceViewModel>()

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
        viewModel.state.observe(viewLifecycleOwner) {
            it?.let { render(it) }
        }
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            selectCountryButton.setOnClickListener {
                findNavController().navigate(R.id.action_selectPlaceFragment_to_selectCountryFragment)
            }
            selectRegionButton.setOnClickListener {
                findNavController().navigate(R.id.action_selectPlaceFragment_to_selectRegionFragment)
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
                val workPlaceState = WorkPlaceState(viewModel.state.value?.country, viewModel.state.value?.region)
                setFragmentResult(APPLY_PLACE_KEY, bundleOf(SELECTED_PLACE_KEY to workPlaceState))
                findNavController().popBackStack()
            }
        }
        setFragmentResultListener(PLACE_REQUEST_KEY) { _, bundle ->
            val workPlaceState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(SELECTED_PLACE_KEY, WorkPlaceState::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getSerializable(SELECTED_PLACE_KEY) as? WorkPlaceState
            }
            viewModel.setPlace(workPlaceState)
        }
    }

    private fun clearCountry() {
        viewModel.clearCountry()
        binding.inputCountry.text = EMPTY_TEXT
    }

    private fun clearRegion() {
        viewModel.clearRegion()
        binding.inputRegion.text = EMPTY_TEXT
    }

    private fun render(state: PlaceState) {
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
