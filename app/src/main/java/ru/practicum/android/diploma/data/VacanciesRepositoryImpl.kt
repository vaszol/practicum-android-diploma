package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converter.VacancyConverter
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import javax.net.ssl.HttpsURLConnection

private const val PAGES = 20

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyRepository {
    override fun searchVacancies(text: String, currency: String, page: Int): Flow<Resource<List<Vacancy>>> = flow {
        val response =
            networkClient.vacancies(VacanciesRequest(text = text, currency = currency, size = PAGES, page = page))
        if (response.resultCode == HttpsURLConnection.HTTP_OK) {
            val vacancies = (response as VacanciesResponse).items.map { VacancyConverter.mapToDomain(it) }
            emit(Resource.Success(vacancies))
        } else {
            emit(Resource.Error(response.resultCode.toString()))
        }
    }
}
