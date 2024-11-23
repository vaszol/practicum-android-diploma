package ru.practicum.android.diploma.presentation.filter

import ru.practicum.android.diploma.domain.models.Industry

data class FilterState(
    val salary: Int? = null,
    val industries: MutableList<Industry> = mutableListOf(),
    val showOnlyWithSalary: Boolean = false
)
