package ru.practicum.android.diploma.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity

interface FavoriteVacancyDao {

    @Insert(entity = FavoriteVacancyEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoritesVacancy(tracks: List<FavoriteVacancyEntity>)

    @Query("DELETE FROM favorite_vacancy_table WHERE id = :id")
    suspend fun deleteFavoriteVacancyById(id: String)

    @Query("SELECT * FROM favorite_vacancy_table ORDER BY primaryKey DESC")
    suspend fun getFavoritesVacancy(): List<FavoriteVacancyEntity>

    @Query("SELECT id FROM favorite_vacancy_table")
    suspend fun getFavoriteVacancyIds(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancy_table WHERE id = :id)")
    suspend fun isVacancyFavorite(id: String): Boolean

}
