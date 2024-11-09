package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Resource
import java.util.stream.Collectors

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
) : VacancyRepository {
    override fun searchVacancies(text: String): Flow<Resource<List<String>>> = flow {
        val response = networkClient.vacancies(VacanciesRequest(text = text, currency = "RUR", size = "20", page = "1"))
        if (response.resultCode == 200) {
            val ids = (response as VacanciesResponse).items.stream().map { e -> e.id }.collect(Collectors.toList())
            emit(Resource.Success(ids))
        } else {
            emit(Resource.Error(response.resultCode.toString()))
        }
    }
}
