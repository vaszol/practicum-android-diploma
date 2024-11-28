package ru.practicum.android.diploma.ui.root.filter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.presentation.filter.place.WorkPlaceState
import ru.practicum.android.diploma.ui.root.RootActivity
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.APPLY_PLACE_KEY
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.PLACE_REQUEST_KEY
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.SELECTED_PLACE_KEY

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

        viewModel.getInitialState()

        setupViews()
        setupListeners()
        observeViewModel()
        setUpFragmentResultListener()

    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        binding.salary.inputType = InputType.TYPE_CLASS_NUMBER
        binding.deleteSalary.isVisible = false

        viewModel.state.value.let { state ->
            // Установка зарплаты
            state.salary?.let {
                binding.salary.setText(it.toString())
            }
            // Установка локации
            if (state.locationString.isNotEmpty()) {
                binding.inputWorkplace.text = state.locationString
                binding.inputWorkplace.isVisible = true
                binding.subtitleWorkplace.isVisible = true
                binding.deleteWorkplace.isVisible = true
            }
            // Установка отрасли
            state.industry?.let { industry ->
                binding.inputIndustry.text = industry.name
                binding.inputIndustry.isVisible = true
                binding.subtitleIndustry.isVisible = true
                binding.deleteIndustry.isVisible = true
            } ?: run {
                binding.inputIndustry.text = ""
                binding.inputIndustry.isVisible = false
                binding.subtitleIndustry.isVisible = false
                binding.deleteIndustry.isVisible = false
            }
        }

        updateButtonVisibility()
    }

    private fun setupListeners() {
        binding.apply {
            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            workplace.setOnClickListener {
                navigateToPlase()
            }
            inputWorkplace.setOnClickListener {
                navigateToPlase()
            }
            deleteWorkplace.setOnClickListener {
                viewModel.updateLocation(null, null)
                inputWorkplace.text = ""
            }
            industry.setOnClickListener {
                findNavController().navigate(R.id.action_filterFragment_to_filterIndustry)
            }
            inputIndustry.setOnClickListener {
                findNavController().navigate(R.id.action_filterFragment_to_filterIndustry)
            }
            deleteIndustry.setOnClickListener {
                viewModel.updateIndustries(null)
                inputIndustry.text = ""
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
                setFragmentResult("applyFilter", bundleOf("updated" to true))
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            reset.setOnClickListener {
                viewModel.resetFilter()
                salary.text.clear()
            }
            setupSalaryListener()
        }
    }

    private fun navigateToPlase() {
        setFragmentResult(
            PLACE_REQUEST_KEY,
            bundleOf(
                SELECTED_PLACE_KEY to WorkPlaceState(
                    viewModel.filter.value.country,
                    viewModel.filter.value.region
                )
            )
        )

        findNavController().navigate(R.id.action_filterFragment_to_selectPlaceFragment)
    }

    private fun setupSalaryListener() {
        binding.apply {
            salary.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
                    override fun afterTextChanged(s: Editable?) {
                        val salaryText = s.toString()
                        val salary = salaryText.takeIf { it.isNotBlank() }?.toIntOrNull()
                        viewModel.updateSalary(salary)

                        val context = expectedSalary.context
                        val colorAccent = context.getThemeColor(org.koin.android.R.attr.colorAccent)
                        val colorOnSecondary =
                            context.getThemeColor(com.google.android.material.R.attr.colorOnSecondary)

                        if (!s.isNullOrEmpty() && !checkBox.isChecked) {
                            expectedSalary.setTextColor(colorAccent)
                            deleteSalary.isVisible = true
                        } else if (!checkBox.isChecked) {
                            expectedSalary.setTextColor(colorOnSecondary)
                            deleteSalary.isVisible = false
                        }
                    }
                }
            )
            getStateFocus(salary)
        }
    }

    private fun observeViewModel() {
        observeFilterState()
        observeButtonVisibility()
    }

    private fun observeFilterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.apply {
                    checkBox.isChecked = state.showOnlyWithSalary
                    deleteSalary.isVisible = state.salary != null

                    if (state.locationString.isNotEmpty()) {
                        inputWorkplace.text = state.locationString
                        inputWorkplace.isVisible = true
                        subtitleWorkplace.isVisible = true
                        deleteWorkplace.isVisible = true
                        binding.workplace.isVisible = false
                    } else {
                        inputWorkplace.text = ""
                        inputWorkplace.isVisible = false
                        subtitleWorkplace.isVisible = false
                        deleteWorkplace.isVisible = false
                        binding.workplace.isVisible = true
                    }

                    state.industry?.let { industry ->
                        inputIndustry.text = industry.name
                        inputIndustry.isVisible = true
                        subtitleIndustry.isVisible = true
                        deleteIndustry.isVisible = true
                        binding.industry.isVisible = false
                    } ?: run {
                        inputIndustry.text = ""
                        inputIndustry.isVisible = false
                        subtitleIndustry.isVisible = false
                        deleteIndustry.isVisible = false
                        binding.industry.isVisible = true
                    }
                }
                updateButtonVisibility()
                getColorExpectedSalary(binding.checkBox)
            }
        }
    }

    private fun observeButtonVisibility() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isApplyButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
                binding.apply.isVisible = isEnabled
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isResetButtonVisible.observe(viewLifecycleOwner) { isVisible ->
                binding.reset.isVisible = isVisible
            }
        }
    }

    private fun setUpFragmentResultListener() {
        // Для Industry
        setFragmentResultListener(FilterFragmentKeys.INDUSTRY_REQUEST_KEY) { _, bundle ->
            val selectedIndustry = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(FilterFragmentKeys.SELECTED_INDUSTRY_KEY, Industry::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getSerializable(FilterFragmentKeys.SELECTED_INDUSTRY_KEY) as? Industry
            }
            viewModel.updateIndustries(selectedIndustry)
        }

        // Для Area (Country и Region)
        setFragmentResultListener(APPLY_PLACE_KEY) { _, bundle ->
            val workPlaceState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(SELECTED_PLACE_KEY, WorkPlaceState::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getSerializable(SELECTED_PLACE_KEY) as? WorkPlaceState
            }

            if (workPlaceState != null) {
                viewModel.updateLocation(workPlaceState.country, workPlaceState.region)
            }
        }
    }

    private fun updateButtonVisibility() {
        val isButtonEnabled = viewModel.isFilterChanged() ||
            viewModel.state.value.industry != null ||
            viewModel.state.value.country != null ||
            viewModel.state.value.region != null ||
            viewModel.state.value.salary != null

        binding.apply.isVisible = isButtonEnabled
        binding.reset.isVisible = viewModel.isResetButtonVisible.value!!
    }

    private fun Context.getThemeColor(attr: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    private fun getColorExpectedSalary(checkBox: CheckBox) {
        if (checkBox.isChecked) {
            binding.expectedSalary.setTextColor(requireContext().getColor(R.color.black))
        } else if (!checkBox.isChecked && binding.salary.text.trim().isNotEmpty()) {
            binding.expectedSalary.setTextColor(
                requireContext().getThemeColor(com.google.android.material.R.attr.colorAccent)
            )
        } else {
            binding.expectedSalary.setTextColor(
                requireContext().getThemeColor(com.google.android.material.R.attr.colorOnSecondary)
            )
        }
    }

    private fun getStateFocus(editText: EditText) {
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.expectedSalary.setTextColor(
                    requireContext().getThemeColor(org.koin.android.R.attr.colorAccent)
                )
            } else {
                binding.expectedSalary.setTextColor(
                    requireContext().getThemeColor(com.google.android.material.R.attr.colorOnSecondary)
                )
            }
        }
    }
}
