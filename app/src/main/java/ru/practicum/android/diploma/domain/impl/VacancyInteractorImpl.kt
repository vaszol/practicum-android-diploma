package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Resource

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
    override fun searchVacancies(text: String): Flow<Pair<List<String>?, String?>> {
        return repository.searchVacancies(text).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }
}
