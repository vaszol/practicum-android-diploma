package ru.practicum.android.diploma.data.dto

data class AreaDto(
    val id: String,
    val parentId: String? = null, //Идентификатор родительского региона
    val name: String,
    val areas: List<AreaDto> = emptyList()  //Список вложенных областей
)
