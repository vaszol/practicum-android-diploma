package ru.practicum.android.diploma.data.db.converter

import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy

object FavoriteVacancyDbConverter {
    fun map(vacancy: Vacancy) = FavoriteVacancyEntity(
        id = vacancy.id,
        name = vacancy.name,
        employerName = vacancy.employerName,
        employerLogoUrl90 = vacancy.employerLogoUrl90,
        areaString = AreaTypeConverter.fromArea(vacancy.area),
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        currency = vacancy.currency
    )

    fun map(vacancy: FavoriteVacancyEntity) = Vacancy(
        id = vacancy.id,
        name = vacancy.name,
        employerName = vacancy.employerName,
        employerLogoUrl90 = vacancy.employerLogoUrl90,
        area = AreaTypeConverter.toArea(vacancy.areaString),
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        currency = vacancy.currency
    )
}
