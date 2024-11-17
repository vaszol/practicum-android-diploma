package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.SearchScreenState
import ru.practicum.android.diploma.util.debouncer.Debouncer
import javax.net.ssl.HttpsURLConnection

class FilterViewModel( ) : ViewModel() {

}
