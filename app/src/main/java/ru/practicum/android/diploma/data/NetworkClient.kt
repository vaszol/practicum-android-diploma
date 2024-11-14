package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Locale
import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun vacancies(dto: Any): Response
    suspend fun vacancy(dto: Any): Response
    suspend fun locales(dto: Any): List<Locale>
}
