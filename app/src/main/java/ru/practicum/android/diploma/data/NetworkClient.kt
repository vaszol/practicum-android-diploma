package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.LocaleDto
import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun getVacancies(dto: Any): Response
    suspend fun getVacancy(dto: Any): Response
    suspend fun getLocales(dto: Any): List<LocaleDto>
    suspend fun getDictionaries(dto: Any): Response
}
