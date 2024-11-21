package ru.practicum.android.diploma.presentation.search

import ru.practicum.android.diploma.domain.models.Vacancy

sealed class SearchScreenState {
    object LoadingFirstPage : SearchScreenState()
    object LoadingNextPage : SearchScreenState()
    object NoInternetFirstPage : SearchScreenState()
    object NoInternetNextPage : SearchScreenState()
    object NothingFound : SearchScreenState()
    object ErrorFirstPage : SearchScreenState()
    object ErrorNextPage : SearchScreenState()
    object EndOfListReached : SearchScreenState()
    data class Results(val resultsList: List<Vacancy>, val totalCount: Int) : SearchScreenState()
}
