package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun getVacancies(dto: Any): Response
    suspend fun getVacancy(dto: Any): Response
    suspend fun getIndustries(dto: Any): List<IndustryDto>
    suspend fun getAreas(dto: Any): List<AreaDto>
}
