package ru.practicum.android.diploma.domain.favorite

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface FavoriteRepository {
    suspend fun addFavoriteVacancy(vacancy: VacancyDetail)
    suspend fun deleteFavoriteVacancyById(id: String)
    suspend fun isVacancyFavorite(id: String): Boolean
    suspend fun getFavoriteVacancyIds(): List<String>
    fun getAllFavoriteVacancies(): Flow<List<VacancyDetail>>
    fun getFavoriteVacancyById(id: String): Flow<VacancyDetail?>
}
