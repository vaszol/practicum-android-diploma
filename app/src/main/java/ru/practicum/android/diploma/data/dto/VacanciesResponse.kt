package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

class VacanciesResponse(
    val items: ArrayList<VacancyDto>,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page")
    val perPage: Int,
) : Response()
