package ru.practicum.android.diploma.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.IO) {
            vacancyInteractor.searchVacancy(
                DetailsVacancyRequest(id = vacancyId)
            ).collect { pair ->
                when {
                    pair.second == NO_INTERNET_CODE -> _screenState.postValue(DetailsScreenState.NoInternet)
                    pair.second != null -> _screenState.postValue(DetailsScreenState.Error(isServerError = true))
                    pair.first == null -> _screenState.postValue(DetailsScreenState.Error(isServerError = false))
                    else -> _screenState.postValue(DetailsScreenState.Content(pair.first!!, pair.first!!.isFavorite))
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

    companion object {
        private const val NO_INTERNET_CODE = "-1"
    }
}
