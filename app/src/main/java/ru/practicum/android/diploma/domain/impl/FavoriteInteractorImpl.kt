package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository
import ru.practicum.android.diploma.domain.models.VacancyDetail

class FavoriteInteractorImpl(
    private val repository: FavoriteRepository
) : FavoriteInteractor {

    override suspend fun addFavoriteVacancy(vacancy: VacancyDetail) {
        repository.addFavoriteVacancy(vacancy)
    }

    override suspend fun deleteFavoriteVacancyById(id: String) {
        repository.deleteFavoriteVacancyById(id)
    }

    override suspend fun isVacancyFavorite(id: String): Boolean {
        return repository.isVacancyFavorite(id)
    }

    override fun getAllFavoriteVacancies(): Flow<List<VacancyDetail>> {
        return repository.getAllFavoriteVacancies()
    }

    override suspend fun getFavoriteVacancyIds(): List<String> {
        return repository.getFavoriteVacancyIds()
    }

    override fun getFavoriteVacancyById(id: String): Flow<VacancyDetail?> {
        return repository.getFavoriteVacancyById(id)
    }
}
