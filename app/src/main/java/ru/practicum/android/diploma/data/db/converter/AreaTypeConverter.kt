package ru.practicum.android.diploma.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Area.Companion.AREA_DEFAULT_VALUE

object AreaTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromArea(area: Area?): String {
        return gson.toJson(
            area ?: Area(
                id = AREA_DEFAULT_VALUE,
                name = AREA_DEFAULT_VALUE,
                parentId = AREA_DEFAULT_VALUE,
                areas = null
            )
        )
    }

    @TypeConverter
    fun toArea(areaString: String): Area {
        return gson.fromJson(areaString, Area::class.java)
    }
}
