package ru.practicum.android.diploma.ui.root.place

import ru.practicum.android.diploma.domain.models.Area

sealed interface AreaState {
    data class Content(val areas: List<Area>): AreaState
    data class NoSuchRegion(val message: String): AreaState
    data class Error(val message: String): AreaState
}
