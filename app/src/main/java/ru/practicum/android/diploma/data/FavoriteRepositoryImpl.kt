package ru.practicum.android.diploma.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.converter.FavoriteVacancyConverter
import ru.practicum.android.diploma.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository
import ru.practicum.android.diploma.domain.models.VacancyDetail
import java.sql.SQLException

class FavoriteRepositoryImpl(private val dao: FavoriteVacancyDao) : FavoriteRepository {
    override suspend fun addFavoriteVacancy(vacancy: VacancyDetail) {
        dao.addFavoriteVacancy(FavoriteVacancyConverter.map(vacancy))
    }

    override suspend fun deleteFavoriteVacancyById(id: String) {
        dao.deleteFavoriteVacancyById(id)
    }

    override suspend fun isVacancyFavorite(id: String): Boolean {
        return dao.isVacancyFavorite(id)
    }

    override suspend fun getFavoriteVacancyIds(): List<String> {
        return dao.getFavoriteVacancyIds()
    }

    override fun getAllFavoriteVacancies(): Flow<List<VacancyDetail>> {
        return try {
            dao.getAllFavoriteVacancies().map { list ->
                list.map { FavoriteVacancyConverter.map(it) }
            }
        } catch (e: SQLException) {
            Log.e("Exception caught in FavoriteViewModel", "Database error occurred: ${e.localizedMessage}", e)
            flowOf(emptyList())
        } catch (e: Exception) {
            Log.e("Exception caught in FavoriteViewModel", "Unexpected error occurred: ${e.localizedMessage}", e)
            flowOf(emptyList())
        }
    }

    override fun getFavoriteVacancyById(id: String): Flow<VacancyDetail?> {
        return dao.getFavoriteVacancyById(id).map { entity ->
            entity?.let { FavoriteVacancyConverter.map(it) }
        }
    }
}
