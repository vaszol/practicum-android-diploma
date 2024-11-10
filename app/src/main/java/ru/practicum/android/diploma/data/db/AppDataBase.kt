package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity

@Database(version = 1, entities = [FavoriteVacancyEntity::class], exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getFavoriteVacancyDao(): FavoriteVacancyDao
}
