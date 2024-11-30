package ru.practicum.android.diploma.util.extentions

import ru.practicum.android.diploma.domain.models.Area

fun Area.flattenAreas(): List<Area> =
    listOf(this) + (areas?.flatMap { it.flattenAreas() } ?: emptyList())
