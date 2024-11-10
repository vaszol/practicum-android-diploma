package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.util.other.Constants.HH_ACCESS_TOKEN

interface HhApi {

    @Headers(
        "Authorization: Bearer $HH_ACCESS_TOKEN",
        "HH-User-Agent: YP Diploma Project (vaszol@mail.ru)"
    )
    @GET("/vacancies")
    suspend fun getVacancies(
        @Query("text") text: String,
        @Query("currency") currency: String,
        @Query("per_page") size: String,
        @Query("page") page: String
    ): VacanciesResponse
}
