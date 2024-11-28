package ru.practicum.android.diploma.presentation.filter.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.models.Area

class SelectPlaceViewModel(
    private val hhInteractor: HhInteractor,
) : ViewModel() {
    private val _state = MutableLiveData(
        PlaceState(
            areas = emptyList(),
            regions = emptyList(),
            showError = false,
            noInternet = false,
            noSuchRegion = false,
            country = null,
            region = null
        )
    )
    val state: LiveData<PlaceState> = _state

    init {
        _state.postValue(
            PlaceState(
                areas = emptyList(),
                regions = emptyList(),
                showError = false,
                noInternet = false,
                noSuchRegion = false,
                country = null,
                region = null
            )
        )
    }

    fun getAreas() {
        viewModelScope.launch {
            hhInteractor.getAreas().collect { pair ->
                when {
                    pair.second != null -> _state.postValue(state.value?.copy(noInternet = true))
                    pair.first.isNullOrEmpty() -> _state.postValue(state.value?.copy(showError = true))
                    else -> updateAreas(pair.first!!)
                }
            }
        }
    }

    private fun updateAreas(areas: List<Area>) {
        val country = state.value?.country
        val region = state.value?.region
        _state.postValue(
            state.value?.copy(
                areas = areas,
                regions = filterRegions(country),
                showError = false,
                noSuchRegion = false,
                country = country,
                region = region
            )
        )
    }

    private fun filterRegions(country: Area?): List<Area> {
        return if (country != null) {
            state.value!!.areas
                .filter { it.id == country.id } // Оставляем только выбранную страну
                .flatMap { it.areas ?: emptyList() } // Собираем регионы выбранной страны
        } else {
            state.value!!.areas.flatMap { it.areas ?: emptyList() } // Собираем регионы всех стран
        }
    }

    fun filterRegions(query: String) {
        if (query.isEmpty()) {
            _state.postValue(state.value?.copy(regions = filterRegions(state.value!!.country)))
        } else {
            val filteredList = state.value!!.regions.filter { it.name.contains(query, ignoreCase = true) }
            if (filteredList.isEmpty()) {
                _state.postValue(state.value?.copy(noSuchRegion = true))
            } else {
                _state.postValue(state.value?.copy(regions = filteredList))
            }
        }
    }

    fun clearCountry() {
        _state.postValue(
            state.value?.copy(
                country = null,
                region = null,
                regions = filterRegions(null)
            )
        )
    }

    fun clearRegion() {
        _state.postValue(state.value?.copy(region = null))
    }

    fun setPlace(workPlaceState: WorkPlaceState?) {
        if (workPlaceState != null) {
            _state.postValue(
                state.value?.copy(
                    country = workPlaceState.country,
                    region = workPlaceState.region,
                    regions = filterRegions(workPlaceState.country),
                )
            )
        }
    }

    fun setCountry(country: Area) {
        _state.postValue(
            state.value?.copy(
                country = country,
                region = null,
                regions = filterRegions(country)
            )
        )
    }

    fun setRegion(region: Area) {
        _state.postValue(state.value?.copy(region = region))
    }
}
