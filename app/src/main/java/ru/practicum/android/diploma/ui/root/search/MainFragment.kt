package ru.practicum.android.diploma.ui.root.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import ru.practicum.android.diploma.presentation.search.SearchScreenState
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.domain.models.Vacancy

class MainFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()
    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private val adapter by lazy { VacancyAdapter(mutableListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchEditText.requestFocus()

        viewModel.searchScreenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchScreenState.Loading -> showLoading()
                SearchScreenState.NoInternet -> showNoInternet()
                SearchScreenState.NothingFound -> showNothingFound()
                is SearchScreenState.Results -> showResults(state.resultsList)
            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    binding.searchMagnifier.setImageResource(R.drawable.ic_close)
                    viewModel.searchDebounce(s.toString())
                } else {
                    binding.searchMagnifier.setImageResource(R.drawable.ic_search)
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
        }

        with(binding) {
            searchRecyclerView.adapter = adapter
            searchEditText.addTextChangedListener(textWatcher)
            searchMagnifier.setOnClickListener {
                searchEditText.setText(EMPTY_TEXT)
                setKeyboardVisibility(searchEditText, false)
            }
        }
    }

    private fun showResults(vacancies: List<Vacancy>) {
        with(binding) {
            searchImgPlaceholder.visibility = View.GONE
            searchError.visibility = View.GONE
            searchProgressBar.visibility = View.GONE
            searchVacancyCount.text =
                resources.getQuantityString(R.plurals.vacancy_postfix, vacancies.size, vacancies.size)
            searchVacancyCount.visibility = View.VISIBLE
            searchRecyclerView.visibility = View.VISIBLE
        }
        adapter.updateData(vacancies)
    }

    private fun showNoInternet() {
        with(binding) {
            searchImgPlaceholder.setImageResource(R.drawable.placeholder_error)
            searchError.setText(R.string.no_internet)
            searchProgressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            searchVacancyCount.visibility = View.GONE
            searchImgPlaceholder.visibility = View.VISIBLE
            searchError.visibility = View.VISIBLE
        }
    }

    private fun showNothingFound() {
        with(binding) {
            searchImgPlaceholder.setImageResource(R.drawable.placeholder_no_vacancy)
            searchError.setText(R.string.nothing_found)
            searchVacancyCount.setText(R.string.no_vacancies)
            searchProgressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            searchImgPlaceholder.visibility = View.VISIBLE
            searchError.visibility = View.VISIBLE
            searchVacancyCount.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        with(binding) {
            searchRecyclerView.visibility = View.GONE
            searchImgPlaceholder.visibility = View.GONE
            searchError.visibility = View.GONE
            searchVacancyCount.visibility = View.GONE
            searchProgressBar.visibility = View.VISIBLE
            setKeyboardVisibility(searchEditText, false)
        }
    }

    private fun setKeyboardVisibility(view: View, isVisible: Boolean) {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (isVisible) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } else {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Test", "onResume")
        with(binding) {
            searchEditText.requestFocus()
            setKeyboardVisibility(searchEditText, true)
        }
    }

    companion object {
        const val EMPTY_TEXT = ""
    }
}
