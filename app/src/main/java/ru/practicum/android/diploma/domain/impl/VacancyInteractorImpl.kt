package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
    override fun searchVacancies(text: String, currency: String, page: Int): Flow<Pair<List<Vacancy>?, String?>> {
        return repository.searchVacancies(text, currency, page).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }
}
