package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.data.details.api.DetailsVacancyRepository
import ru.practicum.android.diploma.domain.api.DetailsVacancyInteractor
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.ui.root.details.models.ErrorDetails

class DetailsVacancyInteractorImpl(
    private val vacancyDetailsRepository: DetailsVacancyRepository
) : DetailsVacancyInteractor {
    override suspend fun getVacancyDetails(request: DetailsVacancyRequest): Pair<VacancyDetail?, String?> {
        return when (val resource = vacancyDetailsRepository.getDetailsVacancy(request)) {
            is Resource.Success -> Pair(resource.data, null)
            is Resource.Error -> Pair(null, resource.message)
        }
    }
}
