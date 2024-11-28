package ru.practicum.android.diploma.ui.root.filter.place

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.presentation.filter.place.PlaceScreenState
import ru.practicum.android.diploma.presentation.filter.place.SelectPlaceViewModel

class SelectRegionFragment : Fragment() {

    private var _binding: FragmentSelectRegionBinding? = null
    private val binding get() = _binding!!

    private val areaAdapter by lazy {
        AreaAdapter {
            viewModel.setRegion(it)
            findNavController().popBackStack()
        }
    }

    private val viewModel by activityViewModel<SelectPlaceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.value?.let { state ->
            if (state is PlaceScreenState.PlaceData) {
                when {
                    state.error -> showError()
                    state.noInternet -> showNoInternet()
                    else -> viewModel.getRegionsList()
                }
            }
        }

        viewModel.areasToDisplay.observe(viewLifecycleOwner) { regions ->
            showContent(regions)
        }

        with(binding) {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            recyclerView.adapter = areaAdapter
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            searchMagnifier.setOnClickListener {
                searchRegionEditText.setText(EMPTY_TEXT)
                viewModel.clearRegion()
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != EMPTY_TEXT) {
                    binding.searchMagnifier.setImageResource(R.drawable.ic_close)
                } else {
                    binding.searchMagnifier.setImageResource(R.drawable.ic_search)
                }
                viewModel.filterRegions(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        }
        textWatcher.let { binding.searchRegionEditText.addTextChangedListener(it) }
    }

    private fun showContent(regions: List<Area>) {
        if (regions.isEmpty()) {
            showNoSuchRegion()
        } else {
            with(binding) {
                searchView.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                placeholder.visibility = View.GONE
                errorMessage.visibility = View.GONE
            }
            areaAdapter.updateList(regions)
        }
    }

    private fun showNoSuchRegion() {
        with(binding) {
            searchView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            placeholderImage.setImageResource(R.drawable.placeholder_no_vacancy)
            errorMessage.setText(R.string.no_such_region)
            placeholder.visibility = View.VISIBLE
            errorMessage.visibility = View.VISIBLE
        }
    }

    private fun showError() {
        with(binding) {
            searchView.visibility = View.GONE
            recyclerView.visibility = View.GONE
            placeholderImage.setImageResource(R.drawable.placeholder_empty_industry_list)
            errorMessage.setText(R.string.error_industry)
            placeholder.visibility = View.VISIBLE
            errorMessage.visibility = View.VISIBLE
            searchMagnifier.visibility = View.GONE
        }
    }

    private fun showNoInternet() {
        with(binding) {
            searchView.visibility = View.GONE
            recyclerView.visibility = View.GONE
            placeholderImage.setImageResource(R.drawable.placeholder_no_internet)
            errorMessage.setText(R.string.no_internet)
            placeholder.visibility = View.VISIBLE
            errorMessage.visibility = View.VISIBLE
            searchMagnifier.visibility = View.GONE
        }
    }

    companion object {
        private const val EMPTY_TEXT = ""
    }
}
