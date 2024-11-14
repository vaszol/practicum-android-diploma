package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.dto.Locale
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacancyInteractor {
    fun searchVacancies(
        text: String,
        currency: String,
        page: Int,
        locale: String,
        host: Host
    ): Flow<Pair<List<Vacancy>?, String?>>

    fun searchVacancy(id: String, locale: String, host: Host): Flow<Pair<Vacancy?, String?>>
    fun searchLocales(locale: String, host: Host): Flow<List<Locale>>
}
