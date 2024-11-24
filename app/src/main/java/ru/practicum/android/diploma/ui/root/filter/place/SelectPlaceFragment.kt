package ru.practicum.android.diploma.ui.root.filter.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                viewModel.clearCountry()
            }
            deleteRegion.setOnClickListener {
                viewModel.clearRegion()
            }
            selectButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun render(state: WorkPlaceState) {
        with(binding) {
            if (state.country != null) {
                countryTitle.visibility = View.GONE
                subtitleCountry.visibility = View.VISIBLE
                inputCountry.text = state.country.name
                inputCountry.visibility = View.VISIBLE
                deleteCountry.visibility = View.VISIBLE
            } else {
                countryTitle.visibility = View.VISIBLE
                subtitleCountry.visibility = View.GONE
                inputCountry.text = EMPTY_TEXT
                inputCountry.visibility = View.GONE
                deleteCountry.visibility = View.GONE
            }

            if (state.region != null) {
                region.visibility = View.GONE
                subtitleRegion.visibility = View.VISIBLE
                inputRegion.text = state.region.name
                inputRegion.visibility = View.VISIBLE
                deleteRegion.visibility = View.VISIBLE
            } else {
                region.visibility = View.VISIBLE
                subtitleRegion.visibility = View.GONE
                inputRegion.text = EMPTY_TEXT
                inputRegion.visibility = View.GONE
                deleteRegion.visibility = View.GONE
            }

            if (state.country != null || state.region != null) {
                selectButton.visibility = View.VISIBLE
            } else {
                selectButton.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val EMPTY_TEXT = ""
    }
}
