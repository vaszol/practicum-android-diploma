package ru.practicum.android.diploma.data.details.api

import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface DetailsVacancyRepository {
    suspend fun getDetailsVacancy(request: DetailsVacancyRequest): Resource<VacancyDetail>
}
