package ru.practicum.android.diploma.domain.models

import java.io.Serializable

data class Industry(
    val id: String,
    val name: String,
    val industries: List<Industry>
) : Serializable
