package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface HhInteractor {
    fun searchVacancies(
        text: String,
        currency: String,
        page: Int,
        locale: String,
        host: Host
    ): Flow<Triple<List<Vacancy>?, String?, Int?>>

    fun searchVacancy(request: DetailsVacancyRequest): Flow<Pair<VacancyDetail?, String?>>
    fun getIndustries(): Flow<List<Industry>>
    fun getAreas(): Flow<List<Area>>
}
