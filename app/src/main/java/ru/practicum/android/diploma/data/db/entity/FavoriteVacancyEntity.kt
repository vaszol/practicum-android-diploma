package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancy_table")
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val employerName: String,
    val employerLogoUrl90: String,
    val areaString: String,
    val experience: String,
    val employment: String,
    val schedule: String,
    val salaryFrom: Int,
    val salaryTo: Int,
    val currency: String,
    val description: String,
    val keySkillsString: String,
    val street: String,
    val building: String,
    val url: String?,
)
