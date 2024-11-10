package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.domain.models.Area

@Entity(tableName = "favorite_vacancy_table")
data class FavoriteVacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int,    // Первичный ключ
    val id: String,
    val name: String,
    val employerName: String,
    val employerLogoUrl90: String,
    val area: Area,
    val experience: String,
    val employment: String,
    val schedule: String,
    val salaryFrom: Int,
    val salaryTo: Int,
    val currency: String,
    val description: String
)
