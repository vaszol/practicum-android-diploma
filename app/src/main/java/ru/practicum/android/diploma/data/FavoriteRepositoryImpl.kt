package ru.practicum.android.diploma.data

import android.database.sqlite.SQLiteException
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.converter.FavoriteVacancyConverter
import ru.practicum.android.diploma.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository
import ru.practicum.android.diploma.domain.models.VacancyDetail

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
        } catch (e: SQLiteException) {
            Log.e("Exception caught in FavoriteRepositoryImpl", "Database error occurred: ${e.localizedMessage}", e)
            flowOf(emptyList())
        } catch (e: IllegalStateException) {
            Log.e("Exception caught in FavoriteRepositoryImpl", "Database state error: ${e.localizedMessage}", e)
            flowOf(emptyList())
        }
    }

    override fun getFavoriteVacancyById(id: String): Flow<VacancyDetail?> {
        return dao.getFavoriteVacancyById(id).map { entity ->
            entity?.let { FavoriteVacancyConverter.map(it) }
        }
    }
}
