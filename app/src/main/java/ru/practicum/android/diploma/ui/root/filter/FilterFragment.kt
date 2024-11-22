package ru.practicum.android.diploma.ui.root.filter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.ui.root.RootActivity

class FilterFragment : Fragment() {
    private val viewModel: FilterViewModel by viewModel()
    private val binding by lazy { FragmentFilterBinding.inflate(layoutInflater) }

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? RootActivity)?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
            View.GONE

        setupListeners()
        setupTextWatchers()
    }

    private fun setupListeners() {
        binding.apply {
            backButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            apply.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            reset.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            industry.setOnClickListener { findNavController().navigate(R.id.action_filterFragment_to_filterIndustry) }
            checkBox.setOnClickListener {
                isClickAllowed = !isClickAllowed
                if (isClickAllowed) {
                    checkBox.setImageResource(R.drawable.ic_check_box_mark)
                } else {
                    checkBox.setImageResource(R.drawable.ic_check_box_unmark)
                }
            }
            deleteSalary.setOnClickListener { salary.text.clear() }
        }
    }

    private fun setupTextWatchers() {
        binding.apply {
            salary.addTextChangedListener(
                afterTextChanged = { s: Editable? ->
                    val context = expectedSalary.context
                    val colorAccent = context.getThemeColor(org.koin.android.R.attr.colorAccent)
                    val colorOnSecondary = context.getThemeColor(com.google.android.material.R.attr.colorOnSecondary)

                    if (!s.isNullOrEmpty()) {
                        expectedSalary.setTextColor(colorAccent)
                        deleteSalary.isVisible = true
                    } else {
                        expectedSalary.setTextColor(colorOnSecondary)
                        deleteSalary.isVisible = false
                    }
                }
            )
        }
    }

    private fun Context.getThemeColor(attr: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}
