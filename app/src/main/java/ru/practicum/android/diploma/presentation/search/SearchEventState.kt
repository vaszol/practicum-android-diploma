package ru.practicum.android.diploma.presentation.search

sealed class SearchEventState {
    data object NoInternetNextPage : SearchEventState()
    data object ErrorNextPage : SearchEventState()
    data object EndOfListReached : SearchEventState()
}
