package ru.practicum.android.diploma.data.converter

import com.google.gson.Gson
import ru.practicum.android.diploma.domain.SharedPreferencesConverter
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

class SharedPreferencesConverterImpl(private val gson: Gson) : SharedPreferencesConverter {
    override fun convertAreaToJson(area: Area): String = gson.toJson(area)

    override fun convertJsonToArea(json: String): Area = gson.fromJson(json, Area::class.java)

    override fun convertListToJson(industries: List<Industry>): String = gson.toJson(industries)

    override fun convertJsonToList(json: String): List<Industry> =
        json.isEmpty().let { gson.fromJson(json, Array<Industry>::class.java) }?.toList() ?: emptyList()
}
