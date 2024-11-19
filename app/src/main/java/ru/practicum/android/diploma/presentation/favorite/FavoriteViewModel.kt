package ru.practicum.android.diploma.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {
    private val _favoriteScreenState = MutableLiveData<FavoriteScreenState>()
    val favoriteScreenState: LiveData<FavoriteScreenState> = _favoriteScreenState

    fun getFavoriteVacancies() {
        viewModelScope.launch {
            favoriteInteractor.getAllFavoriteVacancies().collect { vacancies ->
                if (vacancies.isEmpty()) {
                    _favoriteScreenState.postValue(FavoriteScreenState.NothingInFav)
                } else {
                    _favoriteScreenState.postValue(FavoriteScreenState.FavoriteVacancies(vacancies))
                }
            }
        }
    }
}
