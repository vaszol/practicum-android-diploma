package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun getVacancies(dto: Any): Response
    suspend fun getVacancy(dto: Any): Response
    suspend fun getIndustries(dto: Any): Response
    suspend fun getAreas(dto: Any): Response
}
