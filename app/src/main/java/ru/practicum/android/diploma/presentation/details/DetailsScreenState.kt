package ru.practicum.android.diploma.presentation.details

import ru.practicum.android.diploma.domain.models.VacancyDetail

sealed class DetailsScreenState {
    object Loading : DetailsScreenState()
    object Error : DetailsScreenState()
    data class Content(val vacancy: VacancyDetail, val isFavorite: Boolean) : DetailsScreenState()
}
