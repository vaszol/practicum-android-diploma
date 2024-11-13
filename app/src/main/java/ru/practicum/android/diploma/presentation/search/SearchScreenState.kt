package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    object NoInternet : SearchScreenState()
    object NothingFound : SearchScreenState()
    object Error : SearchScreenState()
    data class Results(val resultsList: List<Vacancy>) : SearchScreenState()
}
