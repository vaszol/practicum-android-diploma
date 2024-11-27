package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDetailDto(
    val id: String,
    val name: String?,
    val employer: Employer, // employer
    val area: AreaDto?, // Регион
    val experience: NameField?, // Опыт работы
    val employment: NameField?, // Тип занятости
    val schedule: NameField?, // График работы
    val salary: Salary?,
    val description: String?, // Описание вакансии в формате HTML
    @SerializedName("key_skills")
    val keySkills: List<NameField>?, // ключевые навыки
    val address: Street?, // Адрес
    @SerializedName("alternate_url")
    val url: String, // ссылка на вакансию
) {

    data class Employer(
        @SerializedName("logo_urls")
        val logoUrls: LogoUrls?,
        val name: String,
    ) {
        data class LogoUrls(
            @SerializedName("90")
            val url90: String?,
        )
    }

    data class NameField(
        val name: String,
    )

    data class Salary(
        val currency: String?,
        val from: Int?,
        val to: Int?,
    )

    data class Street(
        val street: String?,
        val building: String?,
    )
}
