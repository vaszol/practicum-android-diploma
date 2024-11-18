package ru.practicum.android.diploma.data.db.converter

import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

object FavoriteVacancyConverter {
    fun map(vacancy: VacancyDetail) = FavoriteVacancyEntity(
        id = vacancy.id,
        name = vacancy.name,
        employerName = vacancy.employerName,
        employerLogoUrl90 = vacancy.employerLogoUrl90,
        areaString = AreaTypeConverter.fromArea(vacancy.area),
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        currency = vacancy.currency,
        description = vacancy.description,
        keySkillsString = KeySkillsTypeConverter.fromKeySkills(vacancy.keySkills),
        street = vacancy.street,
        building = vacancy.building,
        url = vacancy.url,
        contactsEmail = vacancy.contactsEmail,
        contactsName = vacancy.contactsName,
        experience = vacancy.experience,
        employment = vacancy.employment,
        schedule = vacancy.schedule
    )

    fun map(entity: FavoriteVacancyEntity) = VacancyDetail(
        id = entity.id,
        name = entity.name,
        employerName = entity.employerName,
        employerLogoUrl90 = entity.employerLogoUrl90,
        area = AreaTypeConverter.toArea(entity.areaString),
        salaryFrom = entity.salaryFrom,
        salaryTo = entity.salaryTo,
        currency = entity.currency,
        experience = entity.experience,
        employment = entity.employment,
        schedule = entity.schedule,
        description = entity.description,
        keySkills = KeySkillsTypeConverter.toKeySkills(entity.keySkillsString),
        street = entity.street,
        building = entity.building,
        url = entity.url,
        contactsEmail = entity.contactsEmail,
        contactsName = entity.contactsName
    )

    fun vacancyDetailToVacancy (vacancy: VacancyDetail) = Vacancy(
        id = vacancy.id,
        name = vacancy.name,
        employerName = vacancy.employerName,
        employerLogoUrl90 = vacancy.employerLogoUrl90,
        area = vacancy.area,
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        currency = vacancy.currency
    )
}
