package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacanciesResponse

interface HhApi {

    @GET("/vacancies")
    suspend fun getVacancies(
        @Query("text") text: String,
        @Query("currency") currency: String,
        @Query("per_page") size: String,
        @Query("page") page: String
    ): VacanciesResponse
}
