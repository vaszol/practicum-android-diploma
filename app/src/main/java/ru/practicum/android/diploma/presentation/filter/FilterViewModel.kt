package ru.practicum.android.diploma.presentation.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Industry

class FilterViewModel(
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
    private val hhInteractor: HhInteractor
) : ViewModel() {

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _isApplyButtonEnabled = MutableStateFlow(false)
    val isApplyButtonEnabled: StateFlow<Boolean> = _isApplyButtonEnabled.asStateFlow()

    private val _isResetButtonVisible = MutableStateFlow(false)
    val isResetButtonVisible: StateFlow<Boolean> = _isResetButtonVisible.asStateFlow()

    private var initialFilterState: FilterState

    init {
        val savedSalary = sharedPreferencesInteractor.getSalary()
        val savedIndustries = sharedPreferencesInteractor.getIndustries()
        val savedShowOnlyWithSalary = sharedPreferencesInteractor.getShowOnlyWithSalary()

        initialFilterState = FilterState(
            salary = savedSalary,
            industries = savedIndustries,
            showOnlyWithSalary = savedShowOnlyWithSalary
        )

        _filterState.value = initialFilterState.copy()
        updateButtonStates()
    }

    fun updateSalary(salary: Int?) {
        val currentState = _filterState.value
        val newState = currentState.copy(salary = salary)
        _filterState.value = newState
        updateButtonStates()
    }

    fun updateIndustries(industries: MutableList<Industry>) {
        val currentState = _filterState.value
        val newState = currentState.copy(industries = industries)
        _filterState.value = newState
        updateButtonStates()
    }

    fun toggleShowOnlyWithSalary() {
        val currentState = _filterState.value
        val newState = currentState.copy(
            showOnlyWithSalary = !currentState.showOnlyWithSalary
        )
        _filterState.value = newState
        updateButtonStates()
    }

    private fun updateButtonStates() {
        val currentState = _filterState.value

        _isResetButtonVisible.value =
            currentState.salary != null ||
                currentState.industries.isNotEmpty() ||
                currentState.showOnlyWithSalary

        _isApplyButtonEnabled.value =
            isFilterChanged() && hasAnyFilterSet()
    }

    private fun isFilterChanged(): Boolean {
        return initialFilterState.salary != _filterState.value.salary ||
            initialFilterState.industries != _filterState.value.industries ||
            initialFilterState.showOnlyWithSalary != _filterState.value.showOnlyWithSalary
    }

    private fun hasAnyFilterSet(): Boolean {
        val currentState = _filterState.value
        return currentState.salary != null ||
            currentState.industries.isNotEmpty() ||
            currentState.showOnlyWithSalary
    }

    fun applyFilter() {
        val currentState = _filterState.value

        sharedPreferencesInteractor.setSalary(currentState.salary)
        sharedPreferencesInteractor.setIndustry(currentState.industries)
        sharedPreferencesInteractor.setShowOnlyWithSalary(currentState.showOnlyWithSalary)

        initialFilterState = currentState.copy()

        updateButtonStates()
    }

    fun resetFilter() {
        _filterState.value = FilterState()

        sharedPreferencesInteractor.setSalary(null)
        sharedPreferencesInteractor.setIndustry(mutableListOf())
        sharedPreferencesInteractor.setShowOnlyWithSalary(false)

        initialFilterState = _filterState.value

        updateButtonStates()
    }
}
