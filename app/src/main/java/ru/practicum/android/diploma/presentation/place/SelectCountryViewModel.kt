package ru.practicum.android.diploma.presentation.place

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.ui.root.place.AreaState

class SelectCountryViewModel (
    private val hhInteractor: HhInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<AreaState>()
    fun observeState(): LiveData<AreaState> = stateLiveData

    fun getCountries(){
        viewModelScope.launch {
            hhInteractor.getAreas().collect{ areas ->
                stateLiveData.postValue(AreaState.Content(areas))
                Log.d("AreasList", areas.map { area -> area.name }.toString())
            }
        }
    }
}
