package ru.practicum.android.diploma.data.converter

import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Area.Companion.AREA_DEFAULT_VALUE
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyConverter {

    fun mapToDomain(vacancy: VacancyDto): Vacancy {
        return Vacancy(
            id = vacancy.id,
            name = vacancy.name.orDefaultVacancy(),
            employerName = vacancy.employer.name,
            employerLogoUrl90 = vacancy.employer.logoUrls?.url90.orDefaultVacancy(),
            area = mapToDomain(vacancy.area),
            salaryFrom = vacancy.salary?.from.orDefaultVacancy(),
            salaryTo = vacancy.salary?.to.orDefaultVacancy(),
            currency = vacancy.salary?.currency.orDefaultVacancy()
        )
    }

    fun mapToDomain(vacancy: VacancyDetailDto, isFavorite: Boolean): VacancyDetail {
        return VacancyDetail(
            id = vacancy.id,
            name = vacancy.name.orDefaultVacancy(),
            employerName = vacancy.employer.name,
            employerLogoUrl90 = vacancy.employer.logoUrls?.url90.orDefaultVacancy(),
            area = mapToDomain(vacancy.area),
            experience = vacancy.experience?.name.orDefaultVacancy(),
            employment = vacancy.employment?.name.orDefaultVacancy(),
            schedule = vacancy.schedule?.name.orDefaultVacancy(),
            salaryFrom = vacancy.salary?.from.orDefaultVacancy(),
            salaryTo = vacancy.salary?.to.orDefaultVacancy(),
            currency = vacancy.salary?.currency.orDefaultVacancy(),
            description = vacancy.description.orDefaultVacancy(),
            keySkills = vacancy.keySkills?.map { it.name }.orEmpty(),
            street = vacancy.address?.street.orDefaultVacancy(),
            building = vacancy.address?.building.orDefaultVacancy(),
            url = vacancy.url.orDefaultVacancy(),
            isFavorite = isFavorite
        )
    }

    fun mapToDomain(area: AreaDto?): Area {
        return Area(
            id = area?.id ?: AREA_DEFAULT_VALUE,
            name = area?.name ?: AREA_DEFAULT_VALUE,
            parentId = area?.parentId ?: AREA_DEFAULT_VALUE,
            areas = area?.areas?.map { mapToDomain(it) }.orEmpty()
        )
    }

    fun mapToDomain(industry: IndustryDto?): Industry {
        return Industry(
            id = industry?.id ?: AREA_DEFAULT_VALUE,
            name = industry?.name ?: AREA_DEFAULT_VALUE,
            industries = industry?.industries?.map { mapToDomain(it) }.orEmpty()
        )
    }

    private fun Int?.orDefaultVacancy(): Int = this ?: Vacancy.VACANCY_DEFAULT_INT_VALUE

    private fun String?.orDefaultVacancy(): String = this ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE
}
