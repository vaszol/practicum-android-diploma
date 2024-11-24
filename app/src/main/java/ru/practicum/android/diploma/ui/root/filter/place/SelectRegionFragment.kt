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
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectRegionBinding
import ru.practicum.android.diploma.presentation.filter.place.SelectRegionViewModel

class SelectRegionFragment : Fragment() {

    private var _binding: FragmentSelectRegionBinding? = null
    private val binding get() = _binding!!

    private lateinit var textWatcher: TextWatcher
    private lateinit var areaAdapter: AreaAdapter

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

        viewModel.getRegions(EMPTY_TEXT)

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
            searchMagnifier.setOnClickListener {
                searchRegionEditText.setText(EMPTY_TEXT)
                viewModel.getRegions(EMPTY_TEXT)
            }
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != EMPTY_TEXT) {
                    binding.searchMagnifier.setImageResource(R.drawable.ic_close)
                } else {
                    binding.searchMagnifier.setImageResource(R.drawable.ic_search)
                }
                viewModel.getRegions(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }
        textWatcher.let { binding.searchRegionEditText.addTextChangedListener(it) }
    }

    private fun render(state: AreaState) {
        when (state) {
            is AreaState.Content -> {
                binding.recyclerView.visibility = View.VISIBLE
                binding.placeholder.visibility = View.GONE
                areaAdapter.areas.clear()
                areaAdapter.areas.addAll(state.areas)
                areaAdapter.notifyDataSetChanged()
            }

            is AreaState.NoSuchRegion -> {
                binding.recyclerView.visibility = View.GONE
                binding.placeholderImage.setImageResource(R.drawable.placeholder_no_vacancy)
                binding.errorMessage.text = state.message
                binding.placeholder.visibility = View.VISIBLE
            }

            is AreaState.Error -> {
                binding.recyclerView.visibility = View.GONE
                binding.placeholderImage.setImageResource(R.drawable.placeholder_empty_industry_list)
                binding.errorMessage.text = state.message
                binding.placeholder.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val EMPTY_TEXT = ""
    }
}
