package ru.practicum.android.diploma.presentation.filter

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

data class FilterState(
    val salary: Int? = null,
    val industries: MutableList<Industry> = mutableListOf(),
    val country: Area? = null,
    val region: Area? = null,
    val showOnlyWithSalary: Boolean = false,
    val locationString: String = ""
)
