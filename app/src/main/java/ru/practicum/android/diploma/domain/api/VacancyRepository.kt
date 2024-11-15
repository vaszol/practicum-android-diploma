package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.LocaleDto
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface VacancyRepository {
    fun searchVacancies(
        text: String,
        currency: String,
        page: Int,
        locale: String,
        host: Host
    ): Flow<Resource<List<Vacancy>>>

    fun searchVacancy(id: String, locale: String, host: Host): Flow<Resource<VacancyDetail>>
    fun searchLocales(locale: String, host: Host): Flow<List<LocaleDto>>
}
