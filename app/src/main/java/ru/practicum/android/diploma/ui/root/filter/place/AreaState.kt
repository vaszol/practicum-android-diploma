package ru.practicum.android.diploma.ui.root.filter.place

import ru.practicum.android.diploma.domain.models.Area

sealed interface AreaState {
    data class Content(val areas: List<Area>) : AreaState
    object NoSuchRegion : AreaState
    object Error : AreaState
}
