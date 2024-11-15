package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

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
    @SerializedName("key_skills")
    val keySkills: List<KeySkillDto>?, // Ключевые навыки
    @SerializedName("address.street")
    val street: String?, // Адрес (улица)
    @SerializedName("address.building")
    val building: String? // Адрес (номер дома)
)
