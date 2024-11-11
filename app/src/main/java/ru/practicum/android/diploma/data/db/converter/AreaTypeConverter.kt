package ru.practicum.android.diploma.data.db.converter

import androidx.room.TypeConverter
import ru.practicum.android.diploma.domain.models.Area
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AreaTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromArea(area: Area?): String? {
        return area?.let {
            gson.toJson(it)
        }
    }

    @TypeConverter
    fun toArea(areaString: String?): Area? {
        return areaString?.let {
            val type = object : TypeToken<Area>() {}.type
            gson.fromJson(it, type)
        }
    }
}
