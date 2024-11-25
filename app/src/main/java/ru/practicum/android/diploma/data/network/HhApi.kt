package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailDto

interface HhApi {

    // Поиск по вакансиям
    @GET("/vacancies")
    suspend fun getVacancies(@QueryMap options: Map<String, String>): VacanciesResponse

    // Просмотр вакансии
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(
        @Path("vacancy_id") id: String,
        @Query("locale") locale: String,
        @Query("host") host: String
    ): Response<VacancyDetailDto>

    // Отрасли компаний
    @GET("/industries")
    suspend fun getIndustries(
        @Query("locale") locale: String,
        @Query("host") host: String
    ): List<IndustryDto>

    // Справочник регионов, начиная с указанного
    @GET("/areas")
    suspend fun getAreas(
        @Query("locale") locale: String,
        @Query("host") host: String
    ): List<AreaDto>
}
