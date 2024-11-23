package ru.practicum.android.diploma.ui.root.filter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.ui.root.RootActivity

class FilterFragment : Fragment() {
    private val viewModel: FilterViewModel by viewModel()
    private val binding by lazy { FragmentFilterBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? RootActivity)?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility = View.GONE

        setupViews()
        setupListeners()
        observeViewModel()
    }

    private fun setupViews() {
        binding.salary.inputType = InputType.TYPE_CLASS_NUMBER
        binding.deleteSalary.isVisible = false

        viewModel.filterState.value.salary?.let {
            binding.salary.setText(it.toString())
        }

        binding.apply.isVisible = false
        binding.reset.isVisible = false
    }

    private fun setupListeners() {
        binding.apply {
            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            workplace.setOnClickListener {
                findNavController().navigate(R.id.action_filterFragment_to_selectPlaceFragment)
            }

            industry.setOnClickListener {
                findNavController().navigate(R.id.action_filterFragment_to_filterIndustry)
            }

            checkBox.setOnClickListener {
                viewModel.toggleShowOnlyWithSalary()
            }

            deleteSalary.setOnClickListener {
                salary.text.clear()
                viewModel.updateSalary(null)
            }

            apply.setOnClickListener {
                val currentSalary = salary.text.toString().toIntOrNull()
                viewModel.updateSalary(currentSalary)

                viewModel.applyFilter()
            }

            reset.setOnClickListener {
                viewModel.resetFilter()
                salary.text.clear()
            }

            salary.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

                override fun afterTextChanged(s: Editable?) {
                    val salaryText = s.toString()
                    val salary = salaryText.takeIf { it.isNotBlank() }?.toIntOrNull()
                    viewModel.updateSalary(salary)

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
            })
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filterState.collect { state ->
                binding.apply {
                    checkBox.isChecked = state.showOnlyWithSalary
                    deleteSalary.isVisible = state.salary != null

                    if (state.salary != null) {
                        salary.setText(state.salary.toString())
                    } else {
                        salary.text.clear()
                    }
                }

                updateButtonVisibility()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isApplyButtonEnabled.collect { isEnabled ->
                binding.apply.isVisible = isEnabled
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isResetButtonVisible.collect { isVisible ->
                binding.reset.isVisible = isVisible
            }
        }
    }

    private fun updateButtonVisibility() {
        binding.apply.isVisible = viewModel.isApplyButtonEnabled.value
        binding.reset.isVisible = viewModel.isResetButtonVisible.value
    }

    private fun Context.getThemeColor(attr: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}
