package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.converter.FavoriteVacancyDbConverter
import ru.practicum.android.diploma.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class FavoriteRepositoryImpl(private val dao: FavoriteVacancyDao) : FavoriteRepository {
    override suspend fun addFavoriteVacancy(vacancy: Vacancy) {
        dao.addFavoriteVacancy(FavoriteVacancyDbConverter.map(vacancy))
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

    override fun getAllFavoriteVacancies(): Flow<List<Vacancy>> {
        return dao.getAllFavoriteVacancies().map { list ->
            list.map { FavoriteVacancyDbConverter.map(it) }
        }
    }

    override fun getFavoriteVacancyById(id: String): Flow<Vacancy?> {
        return dao.getFavoriteVacancyById(id).map { entity ->
            entity?.let { FavoriteVacancyDbConverter.map(it) }
        }
    }
}
