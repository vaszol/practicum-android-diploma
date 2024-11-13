package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource

interface VacancyRepository {
    fun searchVacancies(text: String, currency: String, page: String): Flow<Resource<List<String>>>
}
