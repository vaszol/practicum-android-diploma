package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

interface SharedPreferencesConverter {
    fun convertAreaToJson(area: Area): String?
    fun convertJsonToArea(json: String): Area?
    fun convertIndustryToJson(industry: Industry): String?
    fun convertJsonToIndustry(json: String): Industry?
}
