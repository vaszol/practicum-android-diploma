package ru.practicum.android.diploma.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {
    private val _favoriteScreenState = MutableLiveData<FavoriteScreenState>()
    val favoriteScreenState: LiveData<FavoriteScreenState> = _favoriteScreenState

    fun getFavoriteVacancies() {
        viewModelScope.launch {
            val result = runCatching {
                favoriteInteractor.getAllFavoriteVacancies().firstOrNull()
            }

            result.fold(
                onSuccess = { vacancies ->
                    when {
                        vacancies.isNullOrEmpty() -> _favoriteScreenState.postValue(FavoriteScreenState.EmptyList)
                        else -> _favoriteScreenState.postValue(FavoriteScreenState.FavoriteVacancies(vacancies))
                    }
                },
                onFailure = {
                    _favoriteScreenState.postValue(FavoriteScreenState.Error)
                }
            )
        }
    }
}
