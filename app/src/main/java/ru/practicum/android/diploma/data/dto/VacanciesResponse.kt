package ru.practicum.android.diploma.data.dto

class VacanciesResponse(
    val items: ArrayList<VacancyDto>,
    val page: Int,
    val pages: Int,
    val per_page: Int,
) : Response()
