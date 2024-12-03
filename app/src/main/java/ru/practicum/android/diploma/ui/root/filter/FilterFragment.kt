package ru.practicum.android.diploma.ui.root.filter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.filter.FilterState
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.presentation.filter.place.WorkPlaceState
import ru.practicum.android.diploma.ui.root.search.SearchFragment
import ru.practicum.android.diploma.ui.root.search.SearchFragment.Companion.APPLY_FILTER
import ru.practicum.android.diploma.ui.root.search.SearchFragment.Companion.UPDATED
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.APPLY_PLACE_KEY
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.PLACE_REQUEST_KEY
import ru.practicum.android.diploma.util.constants.FilterFragmentKeys.SELECTED_PLACE_KEY

class FilterFragment : Fragment() {
    private val viewModel: FilterViewModel by viewModel()
    private val binding by lazy { FragmentFilterBinding.inflate(layoutInflater) }
    private var query: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isFromSearch = requireArguments().getBoolean(SearchFragment.BUNDLE_KEY)
        if (isFromSearch) {
            query = requireArguments().getString(SearchFragment.SEARCH_QUERY)
        }
        viewModel.setInitialState(isFromSearch)
        setUpFragmentResultListener()
        setupViews()
        setupListeners()
        observeFilterState()
        requireArguments().clear()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        binding.salary.inputType = InputType.TYPE_CLASS_NUMBER
        binding.salary.filters = arrayOf<InputFilter>(MinMaxFilter(1, Int.MAX_VALUE))
        binding.deleteSalary.isVisible = false

        viewModel.observeState.value.let { state ->
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
    }

    private fun setupListeners() {
        binding.apply {
            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            workplace.setOnClickListener {
                navigateToPlace()
            }
            inputWorkplace.setOnClickListener {
                navigateToPlace()
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

                val bundle = bundleOf(
                    UPDATED to true,
                    SearchFragment.SEARCH_QUERY to query
                )
                setFragmentResult(APPLY_FILTER, bundle)

                findNavController().navigate(R.id.action_filterFragment_to_mainFragment)
            }
            reset.setOnClickListener {
                viewModel.resetFilter()
                salary.text.clear()
            }
            setupSalaryListener()
        }
    }

    private fun navigateToPlace() {
        setFragmentResult(
            PLACE_REQUEST_KEY,
            bundleOf(
                SELECTED_PLACE_KEY to WorkPlaceState(
                    viewModel.filterState.value.country,
                    viewModel.filterState.value.region
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
                        updateExpectedSalaryColor()
                        deleteSalary.isVisible = !s.isNullOrEmpty()
                    }
                }
            )

            salary.setOnFocusChangeListener { _, hasFocus ->
                updateExpectedSalaryColor(hasFocus)
            }

            checkBox.setOnClickListener {
                viewModel.toggleShowOnlyWithSalary()
                updateExpectedSalaryColor()
            }
        }
    }

    private fun observeFilterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.observeState.collect { state ->
                binding.apply {
                    checkBox.isChecked = state.showOnlyWithSalary
                    deleteSalary.isVisible = salary.text.trim().isNotEmpty()

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
                    updateButtonVisibility(state)
                    updateExpectedSalaryColor()
                }
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

    private fun updateButtonVisibility(state: FilterState) {
        binding.apply.isVisible = state.apply
        binding.reset.isVisible = state.reset
    }

    private fun Context.getThemeColor(attr: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    private fun updateExpectedSalaryColor(hasFocus: Boolean = false) {
        val context = binding.expectedSalary.context
        val colorAccent = context.getThemeColor(org.koin.android.R.attr.colorAccent)
        val colorOnSecondary = context.getThemeColor(com.google.android.material.R.attr.colorOnSecondary)

        binding.expectedSalary.setTextColor(
            when {
                hasFocus || binding.salary.text.trim().isNotEmpty() -> colorAccent
                else -> colorOnSecondary
            }
        )
    }
}

class MinMaxFilter() : InputFilter {
    private var intMin: Int = 1
    private var intMax: Int = Int.MAX_VALUE

    constructor(minValue: Int, maxValue: Int) : this() {
        this.intMin = minValue
        this.intMax = maxValue
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dStart: Int,
        dEnd: Int
    ): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(intMin, intMax, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}
