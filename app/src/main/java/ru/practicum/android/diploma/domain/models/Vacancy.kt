package ru.practicum.android.diploma.domain.models

import java.io.Serializable

data class Vacancy(
    val id: String,
    val name: String,
    val employerName: String,
    val employerLogoUrl90: String, // URL логотипа (90x90)
    val area: Area?, // Регион
    val experience: String, // Опыт работы
    val employment: String, // Тип занятости
    val schedule: String, // График работы
    val salaryFrom: Int, // Нижний предел зарплаты
    val salaryTo: Int, // Верхний предел зарплаты
    val currency: String, // Код валюты
    val description: String, // Описание вакансии в формате HTML
    val keySkills: List<String>, // Ключевые навыки
    val street: String, // Адрес (улица)
    val building: String // Адрес (номер дома)
) : Serializable {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (id != (other as Vacancy).id) return false
        return true
    }

    companion object {
        private const val serialVersionUID: Long = 1L
        const val VACANCY_DEFAULT_STRING_VALUE = "-1"
        const val VACANCY_DEFAULT_INT_VALUE = -1
        const val CURRENCY_DEFAULT_VALUE = "RUR"
    }
}
