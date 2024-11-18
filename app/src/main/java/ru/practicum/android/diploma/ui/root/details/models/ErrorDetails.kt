package ru.practicum.android.diploma.ui.root.details.models

sealed class ErrorDetails {
    data object DetailsNot : ErrorDetails()
    data object ServerError : ErrorDetails()
}
