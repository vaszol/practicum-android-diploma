package ru.practicum.android.diploma.data.dto

class VacanciesResponse(
    val items: ArrayList<VacancyDto>,
    val found: Int, // Общее количество вакансий
) : Response()
