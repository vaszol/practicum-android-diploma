package ru.practicum.android.diploma.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.DetailsVacancyInteractor
import ru.practicum.android.diploma.domain.models.DetailsVacancyRequest
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.ui.root.details.models.StateVacancyDetails

class DetailsViewModel(
    id: String,
    private val vacancyDetailsInteractor: DetailsVacancyInteractor
    ) : ViewModel() {

    val stateVacancyDetails: MutableLiveData<StateVacancyDetails> = MutableLiveData()
    fun getVacancyState(): LiveData<StateVacancyDetails> = stateVacancyDetails
    init {
        getDetails(id)
    }
    private fun getDetails(id: String) {
        stateVacancyDetails.value = StateVacancyDetails.Loading
        viewModelScope.launch {
            val result = vacancyDetailsInteractor.getVacancyDetails(
                DetailsVacancyRequest(id = id)
            )
            processResult(result.first)
        }
    }

    fun processResult(details: VacancyDetail?) {
        if (details != null) {
            stateVacancyDetails.value = StateVacancyDetails.Content(details)
        }
    }
}
