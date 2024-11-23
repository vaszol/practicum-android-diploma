package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Area
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
        val savedCountry = sharedPreferencesInteractor.getCountry()
        val savedRegion = sharedPreferencesInteractor.getRegion()
        val savedShowOnlyWithSalary = sharedPreferencesInteractor.getShowOnlyWithSalary()

        // Строковое представление локации
        val locationString = createLocationString(savedCountry, savedRegion)

        initialFilterState = FilterState(
            salary = savedSalary,
            industries = savedIndustries,
            country = savedCountry,
            region = savedRegion,
            showOnlyWithSalary = savedShowOnlyWithSalary,
            locationString = locationString
        )

        _filterState.value = initialFilterState.copy()
        updateButtonStates()
    }

    private fun createLocationString(country: Area?, region: Area?): String {
        val locationParts = mutableListOf<String>()

        country?.name?.let { locationParts.add(it) }
        region?.name?.let { locationParts.add(it) }

        return locationParts.joinToString(", ")
    }

    fun updateLocation(country: Area?, region: Area?) {
        val currentState = _filterState.value
        val locationString = createLocationString(country, region)

        val newState = currentState.copy(
            country = country,
            region = region,
            locationString = locationString
        )

        _filterState.value = newState

        country?.let { sharedPreferencesInteractor.setCountry(it) }
        region?.let { sharedPreferencesInteractor.setRegion(it) }

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
                currentState.country != null ||
                currentState.region != null ||
                currentState.showOnlyWithSalary

        _isApplyButtonEnabled.value =
            isFilterChanged() && hasAnyFilterSet()
    }

    private fun isFilterChanged(): Boolean {
        return initialFilterState.salary != _filterState.value.salary ||
            initialFilterState.industries != _filterState.value.industries ||
            initialFilterState.country != _filterState.value.country ||
            initialFilterState.region != _filterState.value.region ||
            initialFilterState.showOnlyWithSalary != _filterState.value.showOnlyWithSalary
    }

    private fun hasAnyFilterSet(): Boolean {
        val currentState = _filterState.value
        return currentState.salary != null ||
            currentState.industries.isNotEmpty() ||
            currentState.country != null ||
            currentState.region != null ||
            currentState.showOnlyWithSalary
    }

    fun applyFilter() {
        val currentState = _filterState.value

        sharedPreferencesInteractor.setSalary(currentState.salary)
        sharedPreferencesInteractor.setIndustry(currentState.industries)
        currentState.country?.let { sharedPreferencesInteractor.setCountry(it) }
        currentState.region?.let { sharedPreferencesInteractor.setRegion(it) }
        sharedPreferencesInteractor.setShowOnlyWithSalary(currentState.showOnlyWithSalary)

        initialFilterState = currentState.copy()

        updateButtonStates()
    }

    fun resetFilter() {
        _filterState.value = FilterState()

        sharedPreferencesInteractor.setSalary(null)
        sharedPreferencesInteractor.setIndustry(mutableListOf())
        sharedPreferencesInteractor.setCountry(null)
        sharedPreferencesInteractor.setRegion(null)
        sharedPreferencesInteractor.setShowOnlyWithSalary(false)

        initialFilterState = _filterState.value

        updateButtonStates()
    }
}
