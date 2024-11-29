package ru.practicum.android.diploma.presentation.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

class FilterViewModel(
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {

    val filterState = MutableStateFlow(FilterState())
    val observeState: StateFlow<FilterState> = filterState.asStateFlow()

    fun updateLocation(country: Area?, region: Area?) {
        filterState.update { state ->
            state.copy(
                country = country,
                region = region,
                locationString = "${country?.name}, ${region?.name}"
            )
        }
        Log.d("FVM updateLocation", filterState.value.toString())
    }

    fun updateSalary(salary: Int?) {
        filterState.update { state -> state.copy(salary = salary) }
        Log.d("FVM updateSalary", filterState.value.toString())
    }

    fun updateIndustries(industry: Industry?) {
        filterState.update { state -> state.copy(industry = industry) }
        Log.d("FVM updateIndustries", filterState.value.toString())
    }

    fun toggleShowOnlyWithSalary() {
        filterState.update { state ->
            state.copy(
                showOnlyWithSalary = !filterState.value.showOnlyWithSalary
            )
        }
        Log.d("FVM toggleSalary", filterState.value.toString())
    }

    fun updateReset() {
        filterState.update { state -> state.copy(reset = hasActiveFilters()) }
        Log.d("FVM updateButtons", filterState.value.toString())
    }

    fun updateApply() {

    }

    fun applyFilter() {
        val currentState = filterState.value

        sharedPreferencesInteractor.setSalary(currentState.salary)
        sharedPreferencesInteractor.setIndustry(currentState.industry)
        sharedPreferencesInteractor.setCountry(currentState.country)
        sharedPreferencesInteractor.setRegion(currentState.region)
        sharedPreferencesInteractor.setShowOnlyWithSalary(currentState.showOnlyWithSalary)
        Log.d("FVM applyFilter", filterState.value.toString())
    }

    fun resetFilter() {
        filterState.update { FilterState() }

        sharedPreferencesInteractor.setSalary(null)
        sharedPreferencesInteractor.setIndustry(null)
        sharedPreferencesInteractor.setCountry(null)
        sharedPreferencesInteractor.setRegion(null)
        sharedPreferencesInteractor.setShowOnlyWithSalary(false)
        Log.d("FVM resetFilter", filterState.value.toString())
    }

    fun hasActiveFilters(): Boolean {
        val currentState = filterState.value
        val hasFilters = currentState.salary != null ||
            currentState.industry != null ||
            currentState.country != null ||
            currentState.region != null ||
            currentState.showOnlyWithSalary
        return hasFilters
    }
}
