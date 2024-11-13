package ru.practicum.android.diploma.ui.root.search

sealed class SearchScreenEvent {
    data object HideKeyboard : SearchScreenEvent()
    data object ClearSearch : SearchScreenEvent()
}
