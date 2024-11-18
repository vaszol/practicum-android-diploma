package ru.practicum.android.diploma.data.details.impl

import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.converter.VacancyConverter
import ru.practicum.android.diploma.data.details.DetailsVacancyDto
import ru.practicum.android.diploma.data.details.api.DetailsVacancyRepository
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetail

class DetailsVacancyRepositoryImpl (
    private val networkClient: NetworkClient,
    private val converterVacancy: VacancyConverter
): DetailsVacancyRepository{
    override suspend fun getDetailsVacancy(request: DetailsVacancyRequest): Resource<VacancyDetail> {
        val setting: HashMap<String, String> = HashMap()
        if (request.locale.isNotEmpty()){
            setting["locale"] = request.locale
        }
        if (request.host.isNotEmpty()){
            setting["host"] = request.host
        }

        val response = networkClient.vacancy(DetailsVacancyDto(request.id, setting))

        return if (response.resultCode == 200){
            Resource.Success(converterVacancy.mapToDomain(response as VacancyDetailDto))
        }else{
            Resource.Error(response.resultCode.toString())
        }
    }
}
