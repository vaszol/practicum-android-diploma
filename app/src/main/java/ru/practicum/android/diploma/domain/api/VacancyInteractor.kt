package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow

interface VacancyInteractor {
    fun searchVacancies(text: String): Flow<Pair<List<String>?, String?>>
}
