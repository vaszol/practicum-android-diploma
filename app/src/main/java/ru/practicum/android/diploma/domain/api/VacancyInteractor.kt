package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacancyInteractor {
    fun searchVacancies(text: String, currency: String, page: Int): Flow<Pair<List<Vacancy>?, String?>>
}
