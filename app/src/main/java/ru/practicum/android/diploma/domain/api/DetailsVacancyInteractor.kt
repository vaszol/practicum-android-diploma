package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.ui.root.details.models.ErrorDetails

interface DetailsVacancyInteractor {
    suspend fun getVacancyDetails(request: DetailsVacancyRequest): Pair<VacancyDetail?, String?>
}
