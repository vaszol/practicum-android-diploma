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
import ru.practicum.android.diploma.presentation.filter.place.PlaceScreenState
import ru.practicum.android.diploma.presentation.filter.place.SelectPlaceViewModel
import ru.practicum.android.diploma.presentation.filter.place.WorkPlaceState
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.APPLY_PLACE_KEY
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.PLACE_REQUEST_KEY
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.SELECTED_PLACE_KEY

class SelectPlaceFragment : Fragment() {
    private var _binding: FragmentSelectPlaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModel<SelectPlaceViewModel>()
    private var pendingWorkPlaceState: WorkPlaceState? = null

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
        viewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let {
                render(it)

                if (state != PlaceScreenState.Loading && pendingWorkPlaceState != null) {
                    // Устанавливаем переданные из настроек фильтрации данные после завершения загрузки
                    viewModel.setPlace(pendingWorkPlaceState)
                    pendingWorkPlaceState = null
                }

                if (state is PlaceScreenState.PlaceData) {
                    val currentState = state
                    if ((currentState.noInternet || currentState.error) && !viewModel.isRetrying) {
                        viewModel.reloadData()
                    }
                }
            }
        }
        setupListeners()
        setFragmentResultListener(PLACE_REQUEST_KEY) { _, bundle ->
            val workPlaceState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(SELECTED_PLACE_KEY, WorkPlaceState::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getSerializable(SELECTED_PLACE_KEY) as? WorkPlaceState
            }
            pendingWorkPlaceState = workPlaceState
            if (viewModel.state.value is PlaceScreenState.PlaceData) {
                workPlaceState?.let { viewModel.setPlace(it) }
                pendingWorkPlaceState = null
            }
        }
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
                val currentState = viewModel.state.value
                if (currentState is PlaceScreenState.PlaceData) {
                    val workPlaceState = WorkPlaceState(currentState.country, currentState.region)
                    setFragmentResult(APPLY_PLACE_KEY, bundleOf(SELECTED_PLACE_KEY to workPlaceState))
                    findNavController().popBackStack()
                }
            }
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

    private fun showPlace(currentData: PlaceScreenState.PlaceData) {
        with(binding) {
            progressBar.visibility = View.GONE
            if (currentData.country != null) {
                countryTitle.isVisible = false
                subtitleCountry.isVisible = true
                inputCountry.text = currentData.country.name
                inputCountry.isVisible = true
                deleteCountry.isVisible = true
            } else {
                countryTitle.visibility = View.VISIBLE
                subtitleCountry.isVisible = false
                inputCountry.text = EMPTY_TEXT
                inputCountry.isVisible = false
                deleteCountry.isVisible = false
            }

            if (currentData.region != null) {
                region.isVisible = false
                subtitleRegion.isVisible = true
                inputRegion.text = currentData.region.name
                inputRegion.isVisible = true
                deleteRegion.isVisible = true
            } else {
                region.isVisible = true
                subtitleRegion.isVisible = false
                inputRegion.text = EMPTY_TEXT
                inputRegion.isVisible = false
                deleteRegion.isVisible = false
            }
            selectButton.isVisible = currentData.country != null || currentData.region != null
        }
    }

    private fun render(state: PlaceScreenState) {
        if (state is PlaceScreenState.Loading) {
            showLoading()
        } else {
            prepareViews()
            state as PlaceScreenState.PlaceData
            if (state.country != null) {
                showPlace(state)
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            countryTitle.visibility = View.GONE
            subtitleCountry.visibility = View.GONE
            inputCountry.visibility = View.GONE
            deleteCountry.visibility = View.GONE
            region.visibility = View.GONE
            subtitleRegion.visibility = View.GONE
            inputRegion.visibility = View.GONE
            deleteRegion.visibility = View.GONE
        }
    }

    private fun prepareViews() {
        with(binding) {
            progressBar.visibility = View.GONE
            countryTitle.visibility = View.VISIBLE
            subtitleCountry.visibility = View.GONE
            inputCountry.visibility = View.GONE
            deleteCountry.visibility = View.GONE
            region.visibility = View.VISIBLE
            subtitleRegion.visibility = View.GONE
            inputRegion.visibility = View.GONE
            deleteRegion.visibility = View.GONE
        }
    }

    companion object {
        private const val EMPTY_TEXT = ""
    }
}
