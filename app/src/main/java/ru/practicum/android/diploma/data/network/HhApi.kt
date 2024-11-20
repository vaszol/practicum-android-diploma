package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.DictionariesDto
import ru.practicum.android.diploma.data.dto.LocaleDto
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailDto

interface HhApi {

    @GET("/vacancies")
    suspend fun getVacancies(
        @Query("text") text: String,
        @Query("currency") currency: String,
        @Query("per_page") size: Int,
        @Query("page") page: Int
    ): VacanciesResponse

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(
        @Path("vacancy_id") id: String,
        @Query("locale") locale: String,
        @Query("host") host: String
    ): Response<VacancyDetailDto>

    @GET("/locales")
    suspend fun getLocales(
        @Query("locale") locale: String,
        @Query("host") host: String
    ): List<LocaleDto>

    @GET("/dictionaries")
    suspend fun getDictionaries(
        @Query("locale") locale: String,
        @Query("host") host: String
    ): Response<DictionariesDto>
}
