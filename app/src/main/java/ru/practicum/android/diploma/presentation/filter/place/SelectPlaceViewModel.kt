package ru.practicum.android.diploma.presentation.filter.place

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Area.Companion.AREA_DEFAULT_VALUE
import javax.net.ssl.HttpsURLConnection

class SelectPlaceViewModel(
    private val hhInteractor: HhInteractor,
) : ViewModel() {
    private val _state = MutableLiveData<PlaceScreenState>()
    val state: LiveData<PlaceScreenState> = _state
    private val emptyPlaceData = PlaceScreenState.PlaceData(
        country = null,
        region = null,
        error = false,
        noInternet = false
    )
    private var allAreas: List<Area> = emptyList()
    private val _areasToDisplay = MutableLiveData<List<Area>>()
    val areasToDisplay: LiveData<List<Area>> = _areasToDisplay

    init {
        getAreas()
    }

    private fun getAreas() {
        _state.value = PlaceScreenState.Loading
        viewModelScope.launch {
            hhInteractor.getAreas().collect { pair ->
                when {
                    pair.second == HttpsURLConnection.HTTP_BAD_REQUEST.toString() ->
                        _state.setValue(emptyPlaceData.copy(error = true))

                    pair.second != null -> _state.setValue(emptyPlaceData.copy(noInternet = true))
                    else -> {
                        allAreas = pair.first!!
                        _state.value = emptyPlaceData
                    }
                }
            }
        }
    }

    fun getRegionsList() {
        val currentState = _state.value
        if (currentState is PlaceScreenState.PlaceData) {
            val regions = if (currentState.country != null) {
                allAreas
                    .filter { it.id == currentState.country.id }
                    .flatMap { it.flattenAreas() }
                    .filter { it.parentId != AREA_DEFAULT_VALUE }
            } else {
                allAreas
                    .flatMap { it.flattenAreas() }
                    .filter { it.parentId != AREA_DEFAULT_VALUE }
            }
            _areasToDisplay.value = regions
        }

    }

    fun filterRegions(query: String) {
        val currentState = _state.value
        if (currentState is PlaceScreenState.PlaceData) {
            if (query.isEmpty()) {
                getRegionsList()
                return
            }

            val filteredList = if (currentState.country != null) {
                allAreas
                    .filter { it.id == currentState.country.id }
                    .flatMap { it.flattenAreas() }
                    .filter { it.name.contains(query, ignoreCase = true) }
            } else {
                allAreas
                    .flatMap { it.flattenAreas() }
                    .filter { it.parentId != AREA_DEFAULT_VALUE }
                    .filter { it.name.contains(query, ignoreCase = true) }
            }
            _areasToDisplay.value = filteredList
        }
    }

    fun clearCountry() {
        val currentState = _state.value
        if (currentState is PlaceScreenState.PlaceData) {
            _state.value = currentState.copy(country = null, region = null)
        }
    }

    fun clearRegion() {
        val currentState = _state.value
        if (currentState is PlaceScreenState.PlaceData) {
            _state.value = currentState.copy(region = null)
        }

    }

    fun setPlace(workPlaceState: WorkPlaceState?) {
        val currentState = _state.value
        if (currentState is PlaceScreenState.PlaceData) {
            if (workPlaceState != null) {
                _state.value = currentState.copy(
                    country = workPlaceState.country,
                    region = workPlaceState.region,
                )
            }
        }
    }

    fun setCountry(country: Area) {
        val currentState = _state.value
        if (currentState is PlaceScreenState.PlaceData) {
            _state.value = currentState.copy(country = country, region = null)
        }
    }

    fun setRegion(region: Area) {
        val parentCountry = findRootArea(region)
        val currentState = _state.value
        if (parentCountry != null && currentState is PlaceScreenState.PlaceData) {
            _state.value = currentState.copy(region = region, country = parentCountry)
        }
    }

    fun getCountriesList() {
        val countriesList = allAreas.filter { it.parentId == AREA_DEFAULT_VALUE }
        _areasToDisplay.value = countriesList
    }

    private fun findRootArea(area: Area): Area? {
        var current = area
        while (current.parentId != AREA_DEFAULT_VALUE) {
            val parent = allAreas
                .flatMap { it.flattenAreas() }
                .firstOrNull { it.id == current.parentId }
            if (parent == null) {
                Log.e(
                    "Exception caught in SelectPlaceViewModel",
                    "Parent area with id=${current.parentId} is not found"
                )
                return null
            }
            current = parent
        }
        return current
    }
}
