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

class SelectRegionViewModel(
    private val hhInteractor: HhInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<AreaState>()
    private var allAreas: List<Area>? = null
    private var allRegions: List<Area> = emptyList()
    fun observeState(): LiveData<AreaState> = stateLiveData

    fun getRegions() {
        viewModelScope.launch {
            hhInteractor.getAreas().collect { pair ->
                when {
                    pair.second != null -> {
                        Log.e("Exception caught in SelectRegionViewModel", "IOException occurred")
                        stateLiveData.postValue(AreaState.Error)
                    }

                    pair.first.isNullOrEmpty() -> {
                        Log.e("Exception caught in SelectRegionViewModel", "regions.isNullOrEmpty")
                        stateLiveData.postValue(AreaState.Error)
                    }

                    else -> {
                        allAreas = pair.first!!
                        val country = getCountry() // Получаю страну из SharedPrefs
                        allRegions = if (country != null) {
                            allAreas!!
                                .filter { it.id == country.id } // Оставляем только выбранную страну
                                .flatMap { it.areas ?: emptyList() } // Собираем регионы выбранной страны
                        } else {
                            allAreas!!.flatMap { it.areas ?: emptyList() } // Собираем регионы всех стран
                        }
                        stateLiveData.postValue(AreaState.Content(allRegions))
                    }
                }
            }
        }
    }

    fun filterRegions(query: String) {
        if (query.isEmpty()) {
            stateLiveData.postValue(AreaState.Content(allRegions))
        } else {
            val filteredList = allRegions.filter { it.name.contains(query, ignoreCase = true) }
            if (filteredList.isEmpty()) {
                stateLiveData.postValue(AreaState.NoSuchRegion)
            } else {
                stateLiveData.postValue(AreaState.Content(filteredList))
            }
        }
    }

    private fun getCountry(): Area? {
        return sharedPreferencesInteractor.getCountry()
    }

    fun setRegion(area: Area) {
        val country = allAreas?.find { it.areas?.contains(area) == true }
        val storedCountry = getCountry()
        if (country != storedCountry) {
            sharedPreferencesInteractor.setCountry(country)
        }
        sharedPreferencesInteractor.setRegion(area)
    }
}
