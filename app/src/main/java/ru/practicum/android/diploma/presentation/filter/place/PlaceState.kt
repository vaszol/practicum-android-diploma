package ru.practicum.android.diploma.presentation.filter.place

import ru.practicum.android.diploma.domain.models.Area

data class PlaceState(
    val areas: List<Area>,
    val regions: List<Area>,
    val country: Area?,
    val region: Area?,
    val showError: Boolean = false,
    val noInternet: Boolean = false,
    val noSuchRegion: Boolean = false,
)
