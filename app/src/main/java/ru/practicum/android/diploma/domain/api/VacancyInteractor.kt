package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow

interface VacancyInteractor {
    fun searchVacancies(text: String, currency: String, page: String): Flow<Pair<List<String>?, String?>>
}
