package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converter.VacancyConverter
import ru.practicum.android.diploma.data.dto.DictionariesRequest
import ru.practicum.android.diploma.data.dto.LocaleDto
import ru.practicum.android.diploma.data.dto.LocaleRequest
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.data.dto.DictionariesResponse
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.Dictionaries
import ru.practicum.android.diploma.domain.models.DictionaryRequest
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail
import javax.net.ssl.HttpsURLConnection

private const val PAGES = 20

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyConverter: VacancyConverter,
    private val repository: FavoriteRepository,
//    private val dictionaryConverter: DictionaryConverter,
) : VacancyRepository {
    override fun searchVacancies(
        text: String,
        currency: String,
        page: Int,
        locale: String,
        host: Host
    ): Flow<Resource<Pair<List<Vacancy>, Int>>> = flow {
        val response =
            networkClient.vacancies(VacanciesRequest(text = text, currency = currency, size = PAGES, page = page))
        if (response.resultCode == HttpsURLConnection.HTTP_OK) {
            val vacanciesResponse = response as VacanciesResponse
            val vacancies = vacanciesResponse.items.map { vacancyConverter.mapToDomain(it) }
            val totalCount = vacanciesResponse.found
            emit(Resource.Success(Pair(vacancies, totalCount))) // Возвращаем список и общее количество
        } else {
            emit(Resource.Error(response.resultCode.toString()))
        }
    }

    override fun searchVacancy(request: DetailsVacancyRequest): Flow<Resource<VacancyDetail?>> = flow {
        val response =
            networkClient.vacancy(VacancyRequest(id = request.id, locale = request.locale, host = request.host))
        val isFavorite = repository.isVacancyFavorite(request.id)
        if (response.resultCode == HttpsURLConnection.HTTP_OK) {
            val vacancy = (response as VacancyResponse).result.let { vacancyConverter.mapToDomain(it, isFavorite) }
            emit(Resource.Success(vacancy))
        } else if (isFavorite) {
            val vacancy = repository.getFavoriteVacancyById(request.id).firstOrNull()
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

    override fun searchDictionaries(request: DictionaryRequest): Flow<Resource<Dictionaries?>> = flow {
        val response =
            networkClient.dictionaries(DictionariesRequest(locale = request.locale, host = request.host))
        if (response.resultCode == HttpsURLConnection.HTTP_OK) {
            val vacancy = (response as DictionariesResponse).result.let { vacancyConverter.mapToDomain(it) }
            emit(Resource.Success(vacancy))
        } else {
            emit(Resource.Error(response.resultCode.toString()))
        }
    }
}
