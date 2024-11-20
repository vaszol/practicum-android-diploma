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
    val contacts: Contacts?, // контакты
) {

    data class Employer(
        @SerializedName("logo_urls") val logoUrls: LogoUrls?,
        @SerializedName("name") val name: String,
    ) {
        data class LogoUrls(
            @SerializedName("90") val url90: String?,
        )
    }

    data class NameField(
        @SerializedName("name") val name: String,
    )

    data class Salary(
        @SerializedName("currency") val currency: String?,
        @SerializedName("from") val from: Int?,
        @SerializedName("to") val to: Int?,
    )

    data class Street(
        @SerializedName("street") val street: String?,
        @SerializedName("building") val building: String?,
    )

    data class Contacts(
        @SerializedName("email") val email: String?,
        @SerializedName("name") val name: String?,
    )
}
