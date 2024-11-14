package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacancyRepository {
    fun searchVacancies(text: String, currency: String, page: Int): Flow<Resource<List<Vacancy>>>
}
