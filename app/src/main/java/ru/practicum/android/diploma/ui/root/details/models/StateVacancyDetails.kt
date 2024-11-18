package ru.practicum.android.diploma.ui.root.details.models

import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

sealed class StateVacancyDetails {
    data class Content(val vacancy: VacancyDetail) : StateVacancyDetails()
    object Loading : StateVacancyDetails()
    object ErrorVacancy : StateVacancyDetails()
    object ErrorServer : StateVacancyDetails()

}
