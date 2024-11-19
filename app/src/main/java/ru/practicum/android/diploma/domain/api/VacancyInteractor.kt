package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.LocaleDto
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface VacancyInteractor {
    fun searchVacancies(
        text: String,
        currency: String,
        page: Int,
        locale: String,
        host: Host
    ): Flow<Triple<List<Vacancy>?, String?, Int?>>

    fun searchVacancy(request: DetailsVacancyRequest): Flow<Pair<VacancyDetail?, String?>>
    fun searchLocales(locale: String, host: Host): Flow<List<LocaleDto>>
}
