package ru.practicum.android.diploma.presentation.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.root.place.AreaState

class SelectRegionViewModel(
    private val hhInteractor: HhInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<AreaState>()
    fun observeState(): LiveData<AreaState> = stateLiveData

    fun getRegions(query: String) {
        viewModelScope.launch {
            hhInteractor.getAreas().collect { areas ->

                val result = ArrayList<Area>()
                val regions = getRegionsByCountry(areas)

                if (query != "") { // Тут логика фильтрации по поиску
                    result.addAll(filter(regions, query))
                    if (result.isEmpty()) {
                        stateLiveData.postValue(AreaState.NoSuchRegion("Такого региона нет"))
                    } else {
                        stateLiveData.postValue(AreaState.Content(result))
                    }
                } else {
                    if (regions.isEmpty()) {
                        stateLiveData.postValue(AreaState.Error("Не удалось получить список"))
                    } else {
                        stateLiveData.postValue(AreaState.Content(regions))
                    }
                }
            }
        }
    }

    private fun getRegionsByCountry(areas: List<Area>): ArrayList<Area> {
        val result = ArrayList<Area>()
        val country = getCountry()

        if (country != null) {
            for (area in areas) {
                if (area.id == country.id) {
                    result.addAll(area.areas!!)
                    return result
                }
            }
        } else {
            for (area in areas) {
                result.addAll(area.areas!!)
            }
        }
        return result
    }

    private fun filter(list: List<Area>, s: String): ArrayList<Area> {
        val result = ArrayList<Area>()
        result.addAll(list.filter { it.name.contains(s, true) })
        return result
    }

    private fun getCountry(): Area? {
        return sharedPreferencesInteractor.getCountry()
    }

    fun setRegion(area: Area) {
        sharedPreferencesInteractor.setRegion(area)
    }
}
