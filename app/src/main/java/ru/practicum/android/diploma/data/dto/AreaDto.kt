package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDto(
    val id: String,
    val name: String?,
    @SerializedName("parent_id")
    val parentId: String? = null, // Идентификатор родительского региона
    val areas: List<AreaDto> = emptyList() // Список вложенных областей
)
