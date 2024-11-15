package ru.practicum.android.diploma.data.dto

data class VacancyRequest(
    val id: String,
    val locale: String = "RU",
    val host: String = "hh.ru"
)
