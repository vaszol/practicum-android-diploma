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
import ru.practicum.android.diploma.util.extentions.flattenAreas
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

    var isRetrying: Boolean = false
        private set

    init {
        _state.value = emptyPlaceData
        getAreas()
    }

    private fun getAreas() {
        val currentState = _state.value
        _state.value = PlaceScreenState.Loading
        viewModelScope.launch {
            isRetrying = true
            hhInteractor.getAreas().collect { pair ->
                handleAreaResponse(pair, currentState)
                isRetrying = false
            }
        }
    }

    private fun handleAreaResponse(pair: Pair<List<Area>?, String?>, currentState: PlaceScreenState?) {
        when {
            pair.second == HttpsURLConnection.HTTP_BAD_REQUEST.toString() ->
                if (currentState is PlaceScreenState.PlaceData) {
                    _state.value = currentState.copy(error = true)
                }

            pair.second != null ->
                if (currentState is PlaceScreenState.PlaceData) {
                    _state.value = currentState.copy(noInternet = true)
                }

            else -> {
                allAreas = pair.first!!
                if (currentState is PlaceScreenState.PlaceData) {
                    _state.value = currentState.copy(error = false, noInternet = false)
                }
            }
        }
    }

    fun getRegionsList() {
        val currentState = _state.value
        if (currentState is PlaceScreenState.PlaceData) {
            val regions = when {
                currentState.country != null -> {
                    val areas = allAreas.filter { it.id == currentState.country.id }
                    if (currentState.country.id == OTHER_REGIONS_ID) {
                        areas.flatMap { // разворачиваются только вложенные элементы первого уровня
                            it.areas ?: emptyList()
                        }
                    } else {
                        areas.flatMap { it.flattenAreas() } // разворачиваются все вложенные элементы
                    }
                }

                else -> {
                    allAreas.flatMap { it.flattenAreas() }
                }
            }.filter { it.parentId != AREA_DEFAULT_VALUE }
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

            val filteredList = when {
                currentState.country != null -> {
                    val areas = allAreas.filter { it.id == currentState.country.id }
                    if (currentState.country.id == OTHER_REGIONS_ID) {
                        areas
                            .flatMap { // разворачиваются только вложенные элементы первого уровня
                                it.areas ?: emptyList()
                            }
                            .filter { it.name.contains(query, ignoreCase = true) }
                    } else {
                        areas
                            .flatMap { it.flattenAreas() } // разворачиваются все вложенные элементы
                            .filter { it.name.contains(query, ignoreCase = true) }
                    }
                }

                else -> {
                    allAreas
                        .flatMap { it.flattenAreas() }
                        .filter { it.parentId != AREA_DEFAULT_VALUE }
                        .filter { it.name.contains(query, ignoreCase = true) }
                }
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
        if (currentState is PlaceScreenState.PlaceData && workPlaceState != null) {
            _state.value = currentState.copy(
                country = workPlaceState.country,
                region = workPlaceState.region,
            )

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
        val countriesList = allAreas
            .filter { it.parentId == AREA_DEFAULT_VALUE }
            .sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
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

    fun reloadData() {
        if (!isRetrying) {
            getAreas()
        }
    }

    companion object {
        private const val OTHER_REGIONS_ID = "1001"
    }
}
