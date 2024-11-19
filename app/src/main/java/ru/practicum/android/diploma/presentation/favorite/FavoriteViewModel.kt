package ru.practicum.android.diploma.presentation.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor
import java.sql.SQLException

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {
    private val _favoriteScreenState = MutableLiveData<FavoriteScreenState>()
    val favoriteScreenState: LiveData<FavoriteScreenState> = _favoriteScreenState

    fun getFavoriteVacancies() {
        viewModelScope.launch {
            val vacancies = safeDbCall {
                favoriteInteractor.getAllFavoriteVacancies().firstOrNull()
            }
            if (vacancies == null) {
                _favoriteScreenState.value = FavoriteScreenState.Error
            } else if (vacancies.isEmpty()) {
                _favoriteScreenState.value = FavoriteScreenState.NothingInFav
            } else {
                _favoriteScreenState.value = FavoriteScreenState.FavoriteVacancies(vacancies)
            }
        }
    }

    private inline fun <T> safeDbCall(action: () -> T): T? {
        return try {
            action()
        } catch (e: SQLException) {
            Log.e("Exception caught in FavoriteViewModel", "Database error occurred: ${e.localizedMessage}", e)
            null
        } catch (e: Exception) {
            Log.e("Exception caught in FavoriteViewModel", "Unexpected error occurred: ${e.localizedMessage}", e)
            null
        }
    }
}
