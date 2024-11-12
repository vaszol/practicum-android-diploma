package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.domain.models.Area

data class AreaDto(
    val id: String,
    val name: String?,
    val parentId: String? = null, // Идентификатор родительского региона
    val areas: List<AreaDto> = emptyList() // Список вложенных областей
)

fun AreaDto.toDomain(): Area =
    Area(
        id = id,
        name = name ?: Area.AREA_DEFAULT_VALUE,
        parentId = parentId ?: Area.AREA_DEFAULT_VALUE,
        areas = if (areas.isEmpty()) emptyList() else areas.map { it.toDomain() }
    )

