package ru.practicum.android.diploma.presentation.filter

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

    fun setInitialState(isFromSearchFragment: Boolean) {
        val currentState = filterState.value

        val salary = if (isFromSearchFragment) sharedPreferencesInteractor.getSalary() else currentState.salary
        val industry = if (isFromSearchFragment) sharedPreferencesInteractor.getIndustry() else currentState.industry
        val country = if (isFromSearchFragment) sharedPreferencesInteractor.getCountry() else currentState.country
        val region = if (isFromSearchFragment) sharedPreferencesInteractor.getRegion() else currentState.region

        val locationString = listOfNotNull(country?.name, region?.name).joinToString(", ")
        val showOnlyWithSalary =
            if (isFromSearchFragment) {
                sharedPreferencesInteractor.getShowOnlyWithSalary()
            } else {
                currentState.showOnlyWithSalary
            }

        val reset = if (isFromSearchFragment) hasPrefs() else hasActiveFilters()

        filterState.update {
            FilterState(
                salary,
                industry,
                country,
                region,
                locationString,
                showOnlyWithSalary,
                reset,
            )
        }
    }

    fun updateLocation(country: Area?, region: Area?) {
        filterState.update { state ->
            val locationString = listOfNotNull(country?.name, region?.name)
                .joinToString(", ")
            state.copy(country = country, region = region, locationString = locationString)
        }
        updateButtonsVisibility()
    }

    fun updateSalary(salary: Int?) {
        filterState.update { state -> state.copy(salary = salary) }
        updateButtonsVisibility()
    }

    fun updateIndustries(industry: Industry?) {
        filterState.update { state -> state.copy(industry = industry) }
        updateButtonsVisibility()
    }

    fun toggleShowOnlyWithSalary() {
        filterState.update { state ->
            state.copy(
                showOnlyWithSalary = !filterState.value.showOnlyWithSalary
            )
        }
        updateButtonsVisibility()
    }

    private fun updateButtonsVisibility() {
        updateResetButtonVisibility()
        updateApplyButtonVisibility()
    }

    private fun updateResetButtonVisibility() {
        filterState.update { state -> state.copy(reset = hasActiveFilters()) }
    }

    private fun updateApplyButtonVisibility() {
        val currentState = filterState.value
        val hasChanges = !compareWithSavedState(currentState)
        filterState.update { state -> state.copy(apply = hasChanges) }
    }

    fun applyFilter() {
        val currentState = filterState.value

        sharedPreferencesInteractor.setSalary(currentState.salary)
        sharedPreferencesInteractor.setIndustry(currentState.industry)
        sharedPreferencesInteractor.setCountry(currentState.country)
        sharedPreferencesInteractor.setRegion(currentState.region)
        sharedPreferencesInteractor.setShowOnlyWithSalary(currentState.showOnlyWithSalary)

        updateButtonsVisibility()
    }

    fun resetFilter() {
        filterState.update { FilterState() }

        sharedPreferencesInteractor.setSalary(null)
        sharedPreferencesInteractor.setIndustry(null)
        sharedPreferencesInteractor.setCountry(null)
        sharedPreferencesInteractor.setRegion(null)
        sharedPreferencesInteractor.setShowOnlyWithSalary(false)
        updateButtonsVisibility()
    }

    private fun hasActiveFilters(): Boolean {

        val currentState = filterState.value
        val hasFilters = currentState.salary != null ||
            currentState.industry != null ||
            currentState.country != null ||
            currentState.region != null ||
            currentState.showOnlyWithSalary
        return hasFilters
    }

    fun hasPrefs(): Boolean {
        return sharedPreferencesInteractor.getSalary() != null ||
            sharedPreferencesInteractor.getIndustry() != null ||
            sharedPreferencesInteractor.getCountry() != null ||
            sharedPreferencesInteractor.getRegion() != null ||
            sharedPreferencesInteractor.getShowOnlyWithSalary()

    }

    private fun compareWithSavedState(currentState: FilterState): Boolean {
        return currentState.salary == sharedPreferencesInteractor.getSalary() &&
            currentState.industry == sharedPreferencesInteractor.getIndustry() &&
            currentState.country == sharedPreferencesInteractor.getCountry() &&
            currentState.region == sharedPreferencesInteractor.getRegion() &&
            currentState.showOnlyWithSalary == sharedPreferencesInteractor.getShowOnlyWithSalary()
    }
}
