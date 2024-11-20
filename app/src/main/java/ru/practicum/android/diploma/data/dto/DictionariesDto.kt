package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class DictionariesDto(

    val currency: List<NameField>?, // Валюты
) {
    data class NameField(
        @SerializedName("name") val name: String,
    )
}
