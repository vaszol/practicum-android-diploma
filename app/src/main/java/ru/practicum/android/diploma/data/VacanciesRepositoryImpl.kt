package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converter.VacancyConverter
import ru.practicum.android.diploma.data.dto.LocaleDto
import ru.practicum.android.diploma.data.dto.LocaleRequest
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail
import javax.net.ssl.HttpsURLConnection

private const val PAGES = 20

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyConverter: VacancyConverter,
) : VacancyRepository {
    override fun searchVacancies(
        text: String,
        currency: String,
        page: Int,
        locale: String,
        host: Host
    ): Flow<Resource<List<Vacancy>>> = flow {
        val response =
            networkClient.vacancies(VacanciesRequest(text = text, currency = currency, size = PAGES, page = page))
        if (response.resultCode == HttpsURLConnection.HTTP_OK) {
            val vacancies = (response as VacanciesResponse).items.map { vacancyConverter.mapToDomain(it) }
            emit(Resource.Success(vacancies))
        } else {
            emit(Resource.Error(response.resultCode.toString()))
        }
    }

    override fun searchVacancy(id: String, locale: String, host: Host): Flow<Resource<VacancyDetail>> = flow {
        val response =
            networkClient.vacancy(VacancyRequest(id = id, locale = locale, host = host.text))
        if (response.resultCode == HttpsURLConnection.HTTP_OK) {
            val vacancy = (response as VacancyResponse).result.let { vacancyConverter.mapToDomain(it) }
            emit(Resource.Success(vacancy))
        } else {
            emit(Resource.Error(response.resultCode.toString()))
        }
    }

    override fun searchLocales(locale: String, host: Host): Flow<List<LocaleDto>> = flow {
        val response =
            networkClient.locales(LocaleRequest(locale = locale, host = host.text))
        emit(response)
    }
}
