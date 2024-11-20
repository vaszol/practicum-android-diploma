package ru.practicum.android.diploma.domain.models

import java.io.Serializable

data class Vacancy(
    val id: String,
    val name: String,
    val employerName: String,
    val employerLogoUrl90: String, // URL логотипа (90x90)
    val area: Area, // Регион
    val salaryFrom: Int, // Нижний предел зарплаты
    val salaryTo: Int, // Верхний предел зарплаты
    val currency: String, // Код валюты
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
