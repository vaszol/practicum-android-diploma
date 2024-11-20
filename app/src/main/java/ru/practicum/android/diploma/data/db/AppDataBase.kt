package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.data.db.converter.AreaTypeConverter
import ru.practicum.android.diploma.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity

@Database(version = 4, entities = [FavoriteVacancyEntity::class], exportSchema = false)
@TypeConverters(AreaTypeConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getFavoriteVacancyDao(): FavoriteVacancyDao
}

