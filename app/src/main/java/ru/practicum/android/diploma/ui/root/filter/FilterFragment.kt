package ru.practicum.android.diploma.ui.root.filter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.presentation.search.SearchScreenState
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.ui.root.search.VacancyAdapter

class FilterFragment : Fragment() {
    private val viewModel: FilterViewModel by viewModel()
    private val binding by lazy { FragmentFilterBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}
