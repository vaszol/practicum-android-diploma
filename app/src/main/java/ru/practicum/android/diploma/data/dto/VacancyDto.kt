package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    val id: String,
    val name: String?,
    @SerializedName("employer.name")
    val employerName: String?, // Название компании
    @SerializedName("employer.logo_urls.90")
    val employerLogoUrl90: String?, // URL логотипа (90x90)
    val area: AreaDto?, // Регион
    @SerializedName("experience.name")
    val experience: String?, // Опыт работы
    @SerializedName("employment.name")
    val employment: String?, // Тип занятости
    @SerializedName("schedule.name")
    val schedule: String?, // График работы
    @SerializedName("salary.from")
    val salaryFrom: Int?, // Нижний предел зарплаты
    @SerializedName("salary.to")
    val salaryTo: Int?, // Верхний предел зарплаты
    @SerializedName("salary.currency")
    val currency: String?, // Код валюты
    val description: String?, // Описание вакансии в формате HTML
    @SerializedName("key_skills")
    val keySkills: List<KeySkillDto>?, //Ключевые навыки
    @SerializedName("address.street")
    val street: String?, // Адрес (улица)
    @SerializedName("address.building")
    val building: String? // Адрес (номер дома)
)
