package ru.practicum.android.diploma.domain.models

import java.io.Serializable

data class Area(
    val id: String,
    val name: String,
    val parentId: String, // Идентификатор родительского региона
    val areas: List<Area>? // Список вложенных областей
) : Serializable {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (id != (other as Area).id) return false
        return true
    }

    companion object {
        const val serialVersionUID = 1L
    }
}
