package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Host
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.debouncer.Debouncer

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor,
) : ViewModel() {
    private var page: Int = 0
    private val _searchScreenState = MutableLiveData<SearchScreenState>()
    val searchScreenState: LiveData<SearchScreenState> = _searchScreenState
    private var latestSearchText: String? = null
    private val debouncer = Debouncer(viewModelScope, SEARCH_DEBOUNCE_DELAY)

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        debouncer.debounce {
            searchRequest(changedText)
        }
    }

    private fun searchRequest(query: String) {
        if (query.isNotEmpty()) {
            _searchScreenState.postValue(SearchScreenState.Loading)

            viewModelScope.launch(Dispatchers.IO) {
                vacancyInteractor.searchVacancies(query, Vacancy.CURRENCY_DEFAULT_VALUE, page, "RU", Host.HH_RU)
                    .collect { pair ->
                        if (pair.second != null) {
                            _searchScreenState.postValue(SearchScreenState.NoInternet)
                        } else if (pair.first.isNullOrEmpty()) {
                            _searchScreenState.postValue(SearchScreenState.NothingFound)
                        } else {
                            _searchScreenState.postValue(SearchScreenState.Results(pair.first!!))
                        }
                    }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

}
