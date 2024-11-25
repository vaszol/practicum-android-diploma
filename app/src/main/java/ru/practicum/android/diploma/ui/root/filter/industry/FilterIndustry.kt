package ru.practicum.android.diploma.ui.root.filter.industry

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.filter.industry.IndustryScreenState
import ru.practicum.android.diploma.presentation.filter.industry.IndustryViewModel
import ru.practicum.android.diploma.ui.root.search.SearchFragment.Companion.EMPTY_TEXT
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys

class FilterIndustry : Fragment() {
    private val binding by lazy { FragmentFilterIndustryBinding.inflate(layoutInflater) }
    private val viewModel: IndustryViewModel by viewModel()
    private val adapter by lazy {
        IndustryAdapter { selectedIndustry ->
            viewModel.selectIndustry(selectedIndustry)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        viewModel.getIndustries()

        viewModel.industryScreenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                IndustryScreenState.Loading -> showLoading()
                IndustryScreenState.Error -> showError()
                is IndustryScreenState.Content -> showIndustries(state.industries)
            }
        }

        viewModel.selectedIndustry.observe(viewLifecycleOwner) { selected ->
            adapter.setSelectedIndustry(selected)
            if (selected != null) {
                binding.buttonIndustry.visibility = View.VISIBLE
            } else {
                binding.buttonIndustry.visibility = View.GONE
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    binding.clearSearchIndustry.visibility = View.VISIBLE
                    binding.searchIconIndustry.visibility = View.GONE
                    adapter.filter(s.toString())
                } else {
                    binding.clearSearchIndustry.visibility = View.GONE
                    binding.searchIconIndustry.visibility = View.VISIBLE
                    adapter.filter("")
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }

        binding.apply {
            backImg.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            buttonIndustry.setOnClickListener {
                val selectedIndustry = viewModel.selectedIndustry.value
                selectedIndustry?.let { industry ->
                    viewModel.saveSelectedIndustry(industry)

                    setFragmentResult(FilterFragmentKeys.INDUSTRY_REQUEST_KEY,
                        bundleOf(FilterFragmentKeys.SELECTED_INDUSTRY_KEY to industry))

                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
            clearSearchIndustry.setOnClickListener {
                edtSearchIndustry.setText(EMPTY_TEXT)
                setKeyboardVisibility(edtSearchIndustry, false)
            }
            edtSearchIndustry.addTextChangedListener(textWatcher)
            recyclerView.addOnScrollListener(
                object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                    override fun onScrolled(
                        recyclerView: androidx.recyclerview.widget.RecyclerView,
                        dx: Int,
                        dy: Int
                    ) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy > 0) { // Скрытие клавиатуры при прокрутке вниз
                            setKeyboardVisibility(edtSearchIndustry, false)
                        }
                    }
                }
            )
        }
    }

    private fun showError() {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            industryError.visibility = View.VISIBLE
        }
    }

    private fun showIndustries(industries: List<Industry>) {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            industryError.visibility = View.GONE
        }
        adapter.updateList(industries)
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            industryError.visibility = View.GONE
        }
    }

    private fun setKeyboardVisibility(view: View, isVisible: Boolean) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (isVisible) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } else {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
