package ru.practicum.android.diploma.presentation.favorite

import ru.practicum.android.diploma.domain.models.VacancyDetail

sealed class FavoriteScreenState {
    object NothingInFav : FavoriteScreenState()
    object Error : FavoriteScreenState()
    data class FavoriteVacancies(val favoriteVacancies: List<VacancyDetail>) : FavoriteScreenState()
}
