package ru.practicum.android.diploma.ui.root.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.presentation.place.SelectRegionViewModel

class SelectRegionFragment : Fragment() {

    private var _binding: FragmentSelectRegionBinding? = null
    private val binding get() = _binding!!

    private lateinit var areaAdapter: AreaAdapter
    private var regions = ArrayList<Area>()

    private val viewModel by viewModel<SelectRegionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.getRegions()

        areaAdapter = AreaAdapter {
            viewModel.setRegion(it)
            findNavController().popBackStack()
        }

        with(binding) {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            recyclerView.adapter = areaAdapter
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun render(state: AreaState) {
        if (state is AreaState.Content) {
            regions.addAll(state.areas)
            areaAdapter.areas.addAll(regions)
            areaAdapter.notifyDataSetChanged()
        }
    }
}
