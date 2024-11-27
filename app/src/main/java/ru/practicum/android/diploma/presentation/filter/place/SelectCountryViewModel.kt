package ru.practicum.android.diploma.presentation.filter.place

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.root.filter.place.AreaState

class SelectCountryViewModel(
    private val hhInteractor: HhInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor
) : ViewModel() {
    private var previousCountry: Area? = null
    private val stateLiveData = MutableLiveData<AreaState>()
    fun observeState(): LiveData<AreaState> = stateLiveData

    fun getCountries() {
        viewModelScope.launch {
            hhInteractor.getAreas().collect { pair ->
                when {
                    pair.second != null -> {
                        Log.e("Exception caught in SelectCountryViewModel", "IOException occurred")
                        stateLiveData.postValue(AreaState.NoInternet)
                    }

                    pair.first.isNullOrEmpty() -> {
                        Log.e("Exception caught in SelectCountryViewModel", "countries.isNullOrEmpty")
                        stateLiveData.postValue(AreaState.Error)
                    }

                    else -> {
                        stateLiveData.postValue(AreaState.Content(pair.first!!))
                    }
                }
            }
        }
    }

    fun setCountry(area: Area) {
        previousCountry = sharedPreferencesInteractor.getCountry()
        if (area != previousCountry) {
            sharedPreferencesInteractor.setCountry(area)
            sharedPreferencesInteractor.setRegion(null)
        }
    }
}
