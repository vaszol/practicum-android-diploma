package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Vacancy

data class VacancyDto(
    val id: String,
    val name: String?,
    val employerName: String?, // employer.name
    val employerLogoUrl90: String?, // URL логотипа (90x90), employer.logo_urls.90
    val area: AreaDto?, // Регион
    val experience: BaseDto?, // Опыт работы
    val employment: BaseDto?, // Тип занятости
    val schedule: BaseDto?, // График работы
    val salaryFrom: Int?, // Нижний предел зарплаты, salary.from
    val salaryTo: Int?, // Верхний предел зарплаты, salary.to
    val currency: String?, // Код валюты, salary.currency
    val description: String?, // Описание вакансии в формате HTML
)

fun VacancyDto.toDomain(): Vacancy =
    Vacancy(
        id = id,
        name = name ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE,
        employerName = employerName ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE,
        employerLogoUrl90 = employerLogoUrl90 ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE,
        area = area?.toDomain() ?: createEmptyArea(),
        experience = experience?.name ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE,
        employment = employment?.name ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE,
        schedule = schedule?.name ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE,
        salaryFrom = salaryFrom ?: Vacancy.VACANCY_DEFAULT_INT_VALUE,
        salaryTo = salaryTo ?: Vacancy.VACANCY_DEFAULT_INT_VALUE,
        currency = currency ?: Vacancy.CURRENCY_DEFAULT_VALUE,
        description = description ?: Vacancy.VACANCY_DEFAULT_STRING_VALUE
    )

private fun createEmptyArea() = Area(
    id = Vacancy.VACANCY_DEFAULT_STRING_VALUE,
    name = Vacancy.VACANCY_DEFAULT_STRING_VALUE,
    parentId = Vacancy.VACANCY_DEFAULT_STRING_VALUE,
    areas = emptyList()
)

