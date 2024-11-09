package ru.practicum.android.diploma.data.dto

data class VacancyDto(
    val id: String,
    val name: String?,
    val employerName: String?,
    val employerLogoUrl90: String?, // URL логотипа (90x90)
    val area: AreaDto?, // Регион
    val experience: String?, // Опыт работы, experienceName
    val employment: String?, // Тип занятости, employmentName
    val schedule: String?, // График работы, scheduleName
    val salaryFrom: Int?, // Нижний предел зарплаты
    val salaryTo: Int?, // Верхний предел зарплаты
    val currency: String?, // Код валюты, salaryCurrency
    val description: String?, // Описание вакансии в формате HTML
)
