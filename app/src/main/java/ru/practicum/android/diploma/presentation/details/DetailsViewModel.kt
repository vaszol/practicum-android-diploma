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

    val _detailsScreenState: MutableLiveData<StateVacancyDetails> = MutableLiveData()
    val detailsScreenState: LiveData<StateVacancyDetails> = _detailsScreenState

    init {
        getDetails(id)
    }

    private fun getDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _detailsScreenState.postValue(StateVacancyDetails.Loading)
            vacancyInteractor.searchVacancy(
                DetailsVacancyRequest(id = id)
            ).collect { pair ->
                if (pair.second != null) {
                    if (pair.second == HttpsURLConnection.HTTP_BAD_REQUEST.toString()) {
                        _detailsScreenState.postValue(StateVacancyDetails.ErrorServer)
                    } else {
                        _detailsScreenState.postValue(StateVacancyDetails.NoInternet)
                    }
                } else {
                    _detailsScreenState.postValue(pair.first?.let { StateVacancyDetails.Content(it) })
                }
            }
        }
    }
}
