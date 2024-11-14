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
        experience = vacancy.experience,
        employment = vacancy.employment,
        schedule = vacancy.schedule,
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        currency = vacancy.currency,
        description = vacancy.description,
        keySkills = vacancy.keySkills.joinToString(", "), // Преобразуем список ключевых навыков в строку
        street = vacancy.street,
        building = vacancy.building
    )

    fun map(vacancy: FavoriteVacancyEntity) = Vacancy(
        id = vacancy.id,
        name = vacancy.name,
        employerName = vacancy.employerName,
        employerLogoUrl90 = vacancy.employerLogoUrl90,
        area = AreaTypeConverter.toArea(vacancy.areaString),
        experience = vacancy.experience,
        employment = vacancy.employment,
        schedule = vacancy.schedule,
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        currency = vacancy.currency,
        description = vacancy.description,
        keySkills = vacancy.keySkills.split(", "),
        street = vacancy.street,
        building = vacancy.building
    )
}
