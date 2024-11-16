package ru.practicum.android.diploma.domain.favorite

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavoriteInteractor {
    suspend fun addFavoriteVacancy(vacancy: Vacancy)
    suspend fun deleteFavoriteVacancyById(id: String)
    suspend fun isVacancyFavorite(id: String): Boolean
    suspend fun getFavoriteVacancyIds(): List<String>
    fun getAllFavoriteVacancies(): Flow<List<Vacancy>>
    fun getFavoriteVacancyById(id: String): Flow<Vacancy?>
}
