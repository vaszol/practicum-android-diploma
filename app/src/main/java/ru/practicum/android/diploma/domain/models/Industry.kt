package ru.practicum.android.diploma.domain.models

import java.io.Serializable

data class Industry(
    val id: String,
    val name: String,
    val industries: List<Industry>
) : Serializable {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (id != (other as Industry).id) return false
        return true
    }

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
