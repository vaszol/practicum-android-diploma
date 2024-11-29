package ru.practicum.android.diploma.presentation.filter

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

data class FilterState(
    val salary: Int? = null,
    val industry: Industry? = null,
    val country: Area? = null,
    val region: Area? = null,
    val locationString: String = "",
    val showOnlyWithSalary: Boolean = false,
    val reset: Boolean = false,
    val apply: Boolean = false
) {
    override fun toString(): String {
        return "$salary, $industry, ${country?.name}, ${region?.name}, $showOnlyWithSalary, $reset, $apply"
    }
}
