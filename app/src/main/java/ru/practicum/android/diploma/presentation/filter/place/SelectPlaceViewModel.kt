package ru.practicum.android.diploma.presentation.filter.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.ui.root.filter.place.WorkPlaceState

class SelectPlaceViewModel(
    private val sharedPreferencesInteractor: SharedPreferencesInteractor
) : ViewModel() {
    private val stateLiveData = MutableLiveData<WorkPlaceState>()
    fun observeState(): LiveData<WorkPlaceState> = stateLiveData

    fun setState() {
        val country = sharedPreferencesInteractor.getCountry()
        val region = sharedPreferencesInteractor.getRegion()
        stateLiveData.postValue(WorkPlaceState(country, region))
    }

    fun clearCountry() {
        sharedPreferencesInteractor.setCountry(null)
        setState()
    }

    fun clearRegion() {
        sharedPreferencesInteractor.setRegion(null)
        setState()
    }
}
