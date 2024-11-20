package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    val id: String,
    val name: String?,
    val employer: Employer, // employer
    val area: AreaDto?, // Регион
    val salary: Salary?,
) {

    data class Employer(
        @SerializedName("logo_urls") val logoUrls: LogoUrls?,
        @SerializedName("name") val name: String,
    ) {
        data class LogoUrls(
            @SerializedName("90") val url90: String?,
        )
    }

    data class Salary(
        @SerializedName("currency") val currency: String?,
        @SerializedName("from") val from: Int?,
        @SerializedName("to") val to: Int?,
    )
}
