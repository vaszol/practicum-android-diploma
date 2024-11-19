package ru.practicum.android.diploma.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.ui.root.details.models.StateVacancyDetails
import javax.net.ssl.HttpsURLConnection

class DetailsViewModel(
    id: String,
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {

    val stateVacancyDetails: MutableLiveData<StateVacancyDetails> = MutableLiveData()
    fun getVacancyState(): LiveData<StateVacancyDetails> = stateVacancyDetails

    init {
        getDetails(id)
    }

    private fun getDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            stateVacancyDetails.postValue(StateVacancyDetails.Loading)
            vacancyInteractor.searchVacancy(
                DetailsVacancyRequest(id = id)
            ).collect { pair ->
                if (pair.second != null) {
                    if (pair.second == HttpsURLConnection.HTTP_BAD_REQUEST.toString()) {
                        stateVacancyDetails.postValue(StateVacancyDetails.ErrorServer)
                    } else {
                        stateVacancyDetails.postValue(StateVacancyDetails.NoInternet)
                    }
                } else {
                    stateVacancyDetails.postValue(pair.first?.let { StateVacancyDetails.Content(it) })
                }
            }
        }
    }
}
