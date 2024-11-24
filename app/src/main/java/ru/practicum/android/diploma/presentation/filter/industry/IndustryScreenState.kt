package ru.practicum.android.diploma.presentation.filter.industry

import ru.practicum.android.diploma.domain.models.Industry

sealed class IndustryScreenState {
    object Loading : IndustryScreenState()
    object Error : IndustryScreenState()
    data class Content(val industries: List<Industry>) : IndustryScreenState()
}
