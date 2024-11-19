package ru.practicum.android.diploma.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.VacancyDetail

class DetailsViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {
    private val _screenState = MutableLiveData<DetailsScreenState>()
    val screenState: LiveData<DetailsScreenState> get() = _screenState

    fun loadVacancyDetails(vacancyId: String) {
        _screenState.value = DetailsScreenState.Loading
        viewModelScope.launch {
            vacancyInteractor.searchVacancy(DetailsVacancyRequest(id = vacancyId)).collect { result ->
                val (vacancy, errorMessage) = result
                val isFavorite = favoriteInteractor.isVacancyFavorite(vacancyId)
                if (vacancy != null) {
                    _screenState.value = DetailsScreenState.Content(vacancy, isFavorite)
                } else if (errorMessage != null) {
                    if (!isFavorite) {
                        _screenState.value = DetailsScreenState.Error(true)
                    } else {
                        val localVacancy = favoriteInteractor.getFavoriteVacancyById(vacancyId).firstOrNull()
                        if (localVacancy != null) {
                            _screenState.value = DetailsScreenState.Content(localVacancy, isFavorite)
                        } else {
                            _screenState.value = DetailsScreenState.Error(false)
                        }
                    }
                }
            }
        }
    }

    fun addToFavorite(vacancy: VacancyDetail) {
        viewModelScope.launch {
            favoriteInteractor.addFavoriteVacancy(vacancy)
        }
    }

    fun removeFromFavorite(vacancy: VacancyDetail) {
        viewModelScope.launch {
            favoriteInteractor.deleteFavoriteVacancyById(vacancy.id)
        }
    }
}
