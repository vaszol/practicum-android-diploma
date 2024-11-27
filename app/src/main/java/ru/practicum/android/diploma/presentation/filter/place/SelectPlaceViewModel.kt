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

class SelectPlaceViewModel(
    private val hhInteractor: HhInteractor,
) : ViewModel() {
    private val _state = MutableLiveData<PlaceState>()
    val state: LiveData<PlaceState> = _state
    private val emptyPlaceState = PlaceState(
        country = null,
        region = null,
        error = false,
        noInternet = false,
        noSuchRegion = false
    )
    private var allAreas: List<Area> = emptyList()
    private val _areasToDisplay = MutableLiveData<List<Area>>()
    val areasToDisplay: LiveData<List<Area>> = _areasToDisplay


    init {
        getAreas()
        _state.value = emptyPlaceState
    }

    private fun getAreas() {
        viewModelScope.launch {
            hhInteractor.getAreas().collect { pair ->
                when {
                    pair.second != null -> _state.setValue(state.value?.copy(error = true))
                    pair.first.isNullOrEmpty() -> _state.setValue(state.value?.copy(noSuchRegion = true))
                    else -> allAreas = pair.first!!
                }
            }
        }
    }

    fun getRegionsList() {
        if (state.value?.country != null) {
            _areasToDisplay.setValue(
                allAreas
                    .filter { it.id == state.value!!.country!!.id } // Оставляем только выбранную страну
                    .flatMap { it.flattenAreas() } // Собираем регионы выбранной страны
                    .filter { it.parentId != AREA_DEFAULT_VALUE } // Убираем саму страну
            )
        } else {
            _areasToDisplay.setValue(
                allAreas
                    .flatMap { it.flattenAreas() }
                    .filter { it.parentId != AREA_DEFAULT_VALUE } // Собираем регионы всех стран (без самих стран)
            )
        }
    }

    fun filterRegions(query: String) {
        if (query.isEmpty()) {
            getRegionsList()
        } else {
            val filteredList =
                if (state.value?.country != null) {
                    allAreas
                        .filter { it.id == state.value!!.country!!.id } // Оставляем только выбранную страну
                        .flatMap { it.flattenAreas() } // Собираем регионы выбранной страны
                        .filter { it.name.contains(query, ignoreCase = true) }

                } else {
                    allAreas
                        .flatMap { it.flattenAreas() }
                        .filter { it.parentId != AREA_DEFAULT_VALUE } // Собираем регионы всех стран (без самих стран)
                        .filter { it.name.contains(query, ignoreCase = true) }
                }
            if (filteredList.isEmpty()) {
                _state.setValue(state.value?.copy(noSuchRegion = true))
            } else {
                _areasToDisplay.setValue(filteredList)
            }
        }
    }

    fun clearCountry() {
        _state.value = state.value?.copy(
            country = null,
            region = null,
        )
    }

    fun clearRegion() {
        _state.value = state.value?.copy(region = null)
    }

    fun setPlace(workPlaceState: WorkPlaceState?) {
        if (workPlaceState != null) {
            _state.value = state.value?.copy(
                country = workPlaceState.country,
                region = workPlaceState.region,
            )
        }
    }

    fun setCountry(country: Area) {
        _state.value = state.value?.copy(
            country = country,
            region = null,
        )
    }

    fun setRegion(region: Area) {
        val parentCountry = findRootArea(region)
        _state.value = state.value?.copy(region = region, country = parentCountry)
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
