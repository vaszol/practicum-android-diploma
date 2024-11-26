package ru.practicum.android.diploma.presentation.filter.place

import ru.practicum.android.diploma.domain.models.Area
import java.io.Serializable

data class WorkPlaceState(
    val country: Area?,
    val region: Area?
) : Serializable {
    override fun hashCode(): Int {
        return country.hashCode() + region.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (country != (other as WorkPlaceState).country) return false
        if (region != other.region) return false
        return true
    }

    companion object {
        const val SERIAL_VERSION_UID = 1L
    }
}
