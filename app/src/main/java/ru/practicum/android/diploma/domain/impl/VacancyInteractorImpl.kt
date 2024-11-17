package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.dto.LocaleDto
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
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
                is Resource.Error ->  Triple(null, result.message, null)
            }
        }
    }

    override fun searchVacancy(id: String, locale: String, host: Host): Flow<Pair<VacancyDetail?, String?>> {
        return repository.searchVacancy(id, locale, host).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }

    override fun searchLocales(locale: String, host: Host): Flow<List<LocaleDto>> {
        return repository.searchLocales(locale, host)
    }
}
