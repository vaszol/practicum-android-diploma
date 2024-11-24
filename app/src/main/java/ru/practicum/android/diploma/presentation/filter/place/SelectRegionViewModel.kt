package ru.practicum.android.diploma.presentation.filter.place

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
    fun observeState(): LiveData<AreaState> = stateLiveData

    fun getRegions() {
        viewModelScope.launch {
            hhInteractor.getAreas().collect { areas ->
                allAreas = areas
                val country = getCountry() // Получаю страну из SharedPrefs
                val regions = if (country != null) {
                    areas
                        .filter { it.id == country.id } // Оставляем только выбранную страну
                        .flatMap { it.areas ?: emptyList() } // Собираем регионы выбранной страны
                } else {
                    areas.flatMap { it.areas ?: emptyList() } // Собираем регионы всех стран
                }
                stateLiveData.postValue(AreaState.Content(regions))
            }
        }
    }

    private fun getCountry(): Area? {
        return sharedPreferencesInteractor.getCountry()
    }

    fun setRegion(area: Area) {
        sharedPreferencesInteractor.setRegion(area)
        val country = allAreas?.find { it.areas?.contains(area) == true }
        sharedPreferencesInteractor.setCountry(country)
    }
}
