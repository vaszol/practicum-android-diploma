package ru.practicum.android.diploma.presentation.filter.place

import ru.practicum.android.diploma.domain.models.Area

sealed class PlaceScreenState {
    data class PlaceData(
        val country: Area?,
        val region: Area?,
        val error: Boolean = false,
        val noInternet: Boolean = false,
    ) : PlaceScreenState()

    object Loading : PlaceScreenState()
}
