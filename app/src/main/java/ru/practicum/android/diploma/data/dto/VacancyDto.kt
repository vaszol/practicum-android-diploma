package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Vacancy

data class VacancyDto(
    val id: String,
    val name: String?,
    val employerName: String?, // employer.name
    val employerLogoUrl90: String?, // URL логотипа (90x90), employer.logo_urls.90
    val area: AreaDto?, // Регион
    @SerializedName("experience.name")
    val experience: String?, // Опыт работы
    @SerializedName("employment.name")
    val employment: String?, // Тип занятости
    @SerializedName("schedule.name")
    val schedule: String?, // График работы
    val salaryFrom: Int?, // Нижний предел зарплаты, salary.from
    val salaryTo: Int?, // Верхний предел зарплаты, salary.to
    val currency: String?, // Код валюты, salary.currency
    val description: String?, // Описание вакансии в формате HTML
)

fun VacancyDto.toDomain(): Vacancy =
    Vacancy(
        id = id,
        name = name.orDefaultVacancy(),
        employerName = employerName.orDefaultVacancy(),
        employerLogoUrl90 = employerLogoUrl90.orDefaultVacancy(),
        area = area?.toDomain() ?: createEmptyArea(),
        experience = experience.orDefaultVacancy(),
        employment = employment.orDefaultVacancy(),
        schedule = schedule.orDefaultVacancy(),
        salaryFrom = salaryFrom.orDefaultVacancy(),
        salaryTo = salaryTo.orDefaultVacancy(),
        currency = currency.orDefaultVacancy(),
        description = description.orDefaultVacancy()
    )

private fun createEmptyArea() = Area(
    id = Vacancy.VACANCY_DEFAULT_STRING_VALUE,
    name = Vacancy.VACANCY_DEFAULT_STRING_VALUE,
    parentId = Vacancy.VACANCY_DEFAULT_STRING_VALUE,
    areas = emptyList()
)

private fun Int?.orDefaultVacancy(): Int = this ?: Vacancy.VACANCY_DEFAULT_INT_VALUE

private fun String?.orDefaultVacancy(): String = this ?: Vacancy.CURRENCY_DEFAULT_VALUE

