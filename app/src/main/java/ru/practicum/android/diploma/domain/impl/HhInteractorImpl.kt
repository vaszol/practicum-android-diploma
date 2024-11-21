package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.HhRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

class HhInteractorImpl(private val repository: HhRepository) : HhInteractor {
    override fun searchVacancies(
        text: String,
        currency: String,
        page: Int,
        locale: String,
        host: Host
    ): Flow<Triple<List<Vacancy>?, String?, Int?>> {
        return repository.searchVacancies(text, currency, page, locale, host).map { result ->
            when (result) {
                is Resource.Success -> Triple(result.data?.first, null, result.data?.second)
                is Resource.Error -> Triple(null, result.message, null)
            }
        }
    }

    override fun searchVacancy(request: DetailsVacancyRequest): Flow<Pair<VacancyDetail?, String?>> {
        return repository.searchVacancy(request).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }

    override fun getIndustries(): Flow<List<Industry>> {
        return repository.getIndustries()
    }

    override fun getAreas(): Flow<List<Area>> {
        return repository.getAreas()
    }
}
