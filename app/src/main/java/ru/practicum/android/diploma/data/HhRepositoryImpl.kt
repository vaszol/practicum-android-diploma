package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.converter.VacancyConverter
import ru.practicum.android.diploma.data.dto.LocaleRequest
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.domain.api.HhRepository
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail
import javax.net.ssl.HttpsURLConnection

private const val PAGES = 20

class HhRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyConverter: VacancyConverter,
    private val repository: FavoriteRepository,
) : HhRepository {
    override fun searchVacancies(
        text: String,
        currency: String,
        page: Int,
        locale: String,
        host: Host
    ): Flow<Resource<Pair<List<Vacancy>, Int>>> = flow {
        val response =
            networkClient.getVacancies(VacanciesRequest(text = text, currency = currency, size = PAGES, page = page))
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
            networkClient.getVacancy(VacancyRequest(id = request.id, locale = request.locale, host = request.host))
        val isFavorite = repository.isVacancyFavorite(request.id)
        when {
            response.resultCode == HttpsURLConnection.HTTP_OK -> {
                val vacancy =
                    (response as VacancyResponse).result.let { vacancyConverter.mapToDomain(it!!, isFavorite) }
                emit(Resource.Success(vacancy))
            }

            isFavorite -> {
                val vacancy = repository.getFavoriteVacancyById(request.id).firstOrNull()
                emit(Resource.Success(vacancy))
            }

            else -> {
                emit(Resource.Error(response.resultCode.toString()))
            }
        }
    }

    override fun getIndustries(): Flow<List<Industry>> = flow {
        val responseList = networkClient.getIndustries(LocaleRequest())
        val flatList = responseList.flatMap { topLevelIndustry ->
            topLevelIndustry.industries?.map { vacancyConverter.mapToDomain(it) } ?: emptyList()
        }
        emit(flatList)
    }

    override fun getAreas(): Flow<List<Area>> = flow {
        val responseList = networkClient.getAreas(LocaleRequest())
        val list = responseList.map { vacancyConverter.mapToDomain(it) }
        emit(list)
    }
}
