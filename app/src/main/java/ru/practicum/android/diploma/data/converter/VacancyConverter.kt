package ru.practicum.android.diploma.data.converter

import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Area.Companion.AREA_DEFAULT_VALUE
import ru.practicum.android.diploma.domain.models.Vacancy

object VacancyConverter {

    fun mapToDomain(vacancy: VacancyDto): Vacancy {
        return Vacancy(
            id = vacancy.id,
            name = vacancy.name.orDefaultVacancy(),
            employerName = vacancy.employerName.orDefaultVacancy(),
            employerLogoUrl90 = vacancy.employerLogoUrl90.orDefaultVacancy(),
            area = mapToDomain(vacancy.area),
            experience = vacancy.experience.orDefaultVacancy(),
            employment = vacancy.employment.orDefaultVacancy(),
            schedule = vacancy.schedule.orDefaultVacancy(),
            salaryFrom = vacancy.salaryFrom.orDefaultVacancy(),
            salaryTo = vacancy.salaryTo.orDefaultVacancy(),
            currency = vacancy.currency.orDefaultVacancy(),
            description = vacancy.description.orDefaultVacancy(),
            keySkills = vacancy.keySkills?.map { it.name } ?: emptyList(),
            street = vacancy.street.orDefaultVacancy(),
            building = vacancy.building.orDefaultVacancy()
        )
    }

    fun mapToDomain(vacancy: VacancyResponse): Vacancy {
        return Vacancy(
            id = vacancy.id,
            name = vacancy.name.orDefaultVacancy(),
            employerName = vacancy.employerName.orDefaultVacancy(),
            employerLogoUrl90 = vacancy.employerLogoUrl90.orDefaultVacancy(),
            area = vacancy.area?.let { mapToDomain(it) },
            experience = vacancy.experience.orDefaultVacancy(),
            employment = vacancy.employment.orDefaultVacancy(),
            schedule = vacancy.schedule.orDefaultVacancy(),
            salaryFrom = vacancy.salaryFrom.orDefaultVacancy(),
            salaryTo = vacancy.salaryTo.orDefaultVacancy(),
            currency = vacancy.currency.orDefaultVacancy(),
            description = vacancy.description.orDefaultVacancy()
        )
    }

    private fun mapToDomain(area: AreaDto?): Area {
        return Area(
            id = area?.id ?: AREA_DEFAULT_VALUE,
            name = area?.name ?: AREA_DEFAULT_VALUE,
            parentId = area?.parentId ?: AREA_DEFAULT_VALUE,
            areas = if (area?.areas.isNullOrEmpty()) {
                emptyList()
            } else {
                area?.areas?.map { mapToDomain(it) }
            }
        )
    }

    private fun Int?.orDefaultVacancy(): Int = this ?: Vacancy.VACANCY_DEFAULT_INT_VALUE

    private fun String?.orDefaultVacancy(): String = this ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE
}
