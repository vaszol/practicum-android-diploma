package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.filter.industry.IndustryViewModel

class FilterViewModel(
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
    industryViewModel: IndustryViewModel
) : ViewModel() {

    val filter = MutableStateFlow(FilterState())
    val state: StateFlow<FilterState> = filter.asStateFlow()

    private val _isApplyButtonEnabled = MutableStateFlow(false)
    val isApplyButtonEnabled: StateFlow<Boolean> = _isApplyButtonEnabled.asStateFlow()

    private val _isResetButtonVisible = MutableStateFlow(false)
    val isResetButtonVisible: StateFlow<Boolean> = _isResetButtonVisible.asStateFlow()

    private var initialFilterState: FilterState = FilterState()

    init {
        industryViewModel.selectedIndustry.observeForever { selectedIndustry ->
            updateIndustries(selectedIndustry)
        }
        getInitialState()
        updateButtonStates()
    }

    fun getInitialState() {
        val savedSalary = sharedPreferencesInteractor.getSalary()
        val savedIndustry = sharedPreferencesInteractor.getIndustry()
        val savedCountry = sharedPreferencesInteractor.getCountry()
        val savedRegion = sharedPreferencesInteractor.getRegion()
        val savedShowOnlyWithSalary = sharedPreferencesInteractor.getShowOnlyWithSalary()
        val locationString = createLocationString(savedCountry, savedRegion)

        initialFilterState = FilterState(
            salary = savedSalary,
            industry = savedIndustry,
            country = savedCountry,
            region = savedRegion,
            showOnlyWithSalary = savedShowOnlyWithSalary,
            locationString = locationString
        )

        filter.value = initialFilterState.copy()
    }

    fun updateLocation(country: Area?, region: Area?) {
        val currentState = filter.value
        val locationString = createLocationString(country, region)

        val newState = currentState.copy(
            country = country,
            region = region,
            locationString = locationString
        )

        filter.value = newState

        sharedPreferencesInteractor.setCountry(country)
        sharedPreferencesInteractor.setRegion(region)

        updateButtonStates()
    }

    fun updateSalary(salary: Int?) {
        val currentState = filter.value
        val newState = currentState.copy(salary = salary)
        filter.value = newState
        updateButtonStates()
    }

    fun updateIndustries(industry: Industry?) {
        val currentState = filter.value
        val newState = currentState.copy(industry = industry)
        filter.value = newState
        sharedPreferencesInteractor.setIndustry(industry)
    }

    fun toggleShowOnlyWithSalary() {
        val currentState = filter.value
        val newState = currentState.copy(
            showOnlyWithSalary = !currentState.showOnlyWithSalary
        )
        filter.value = newState
        updateButtonStates()
    }

    fun applyFilter() {
        val currentState = filter.value

        sharedPreferencesInteractor.setSalary(currentState.salary)

        sharedPreferencesInteractor.setIndustry(currentState.industry)

        sharedPreferencesInteractor.setCountry(currentState.country)
        sharedPreferencesInteractor.setRegion(currentState.region)

        currentState.country?.let { sharedPreferencesInteractor.setCountry(it) }
        currentState.region?.let { sharedPreferencesInteractor.setRegion(it) }

        sharedPreferencesInteractor.setShowOnlyWithSalary(currentState.showOnlyWithSalary)

        initialFilterState = currentState.copy()

        updateButtonStates()
    }

    fun resetFilter() {
        filter.value = FilterState()

        sharedPreferencesInteractor.setSalary(null)
        sharedPreferencesInteractor.setIndustry(null)
        sharedPreferencesInteractor.setCountry(null)
        sharedPreferencesInteractor.setRegion(null)
        sharedPreferencesInteractor.setShowOnlyWithSalary(false)

        initialFilterState = filter.value

        updateButtonStates()
    }

    private fun createLocationString(country: Area?, region: Area?): String {
        val locationParts = mutableListOf<String>()

        country?.name?.let { locationParts.add(it) }
        region?.name?.let { locationParts.add(it) }

        return locationParts.joinToString(", ")
    }

    private fun updateButtonStates() {
        val currentState = filter.value

        _isResetButtonVisible.value = listOf<Any?>(
            currentState.salary,
            currentState.industry,
            currentState.country,
            currentState.region,
            currentState.showOnlyWithSalary.takeIf { it }
        ).any { it != null }

        _isApplyButtonEnabled.value = isFilterChanged() ||
            currentState.industry != null ||
            currentState.country != null ||
            currentState.region != null
    }

    fun isFilterChanged(): Boolean {
        return initialFilterState.salary != filter.value.salary ||
            initialFilterState.industry != filter.value.industry ||
            initialFilterState.country != filter.value.country ||
            initialFilterState.region != filter.value.region ||
            initialFilterState.showOnlyWithSalary != filter.value.showOnlyWithSalary
    }

    fun hasActiveFilters(): Boolean {
        val currentState = filter.value
        val hasFilters = currentState.salary != null ||
            currentState.industry != null ||
            currentState.country != null ||
            currentState.region != null ||
            currentState.showOnlyWithSalary
        return hasFilters
    }
}
