package ru.practicum.android.diploma.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.models.Area

class AreaTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromArea(area: Area?): String? {
        return gson.toJson(area)
    }

    @TypeConverter
    fun toArea(areaString: String?): Area? {
        return gson.fromJson(areaString, Area::class.java)
    }
}
