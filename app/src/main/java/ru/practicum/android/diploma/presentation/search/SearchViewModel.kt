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
import javax.net.ssl.HttpsURLConnection

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor,
) : ViewModel() {
    private var page: Int = 0
    private val _searchScreenState = MutableLiveData<SearchScreenState>()
    val searchScreenState: LiveData<SearchScreenState> = _searchScreenState
    private var latestSearchText: String? = null
    private val currentVacancies = mutableListOf<Vacancy>()
    private var isLoadingNextPage = false
    private var isEndOfListReached = false
    private val debouncer = Debouncer(viewModelScope, SEARCH_DEBOUNCE_DELAY)

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        page = 0
        isEndOfListReached = false
        debouncer.debounce {
            searchRequest()
        }
    }

    private fun searchRequest() {
        if (!latestSearchText.isNullOrEmpty()) {
            if (page == 0) {
                _searchScreenState.postValue(SearchScreenState.LoadingFirstPage)
                performSearchRequest()
            } else {
                _searchScreenState.postValue(SearchScreenState.LoadingNextPage)
                performSearchRequest()
            }
        }
    }

    private fun performSearchRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            vacancyInteractor.searchVacancies(
                latestSearchText!!,
                Vacancy.CURRENCY_DEFAULT_VALUE,
                page,
                "RU",
                Host.HH_RU
            ).collect { result ->
                val (vacancies, errorMessage, totalCount) = result

                if (errorMessage != null) {
                    handleSearchError(page == 0, errorMessage)
                    if (page > 0) page--
                } else if (vacancies.isNullOrEmpty()) {
                    isEndOfListReached = true
                    if (page > 0) {
                        _searchScreenState.postValue(SearchScreenState.EndOfListReached)
                    } else {
                        _searchScreenState.postValue(SearchScreenState.NothingFound)
                    }
                } else {
                    if (page == 0) {
                        currentVacancies.clear()
                    }

                    val newVacancies = vacancies.filterNot { vacancy ->
                        currentVacancies.any { it.id == vacancy.id }
                    }

                    currentVacancies.addAll(newVacancies)
                    _searchScreenState.postValue(SearchScreenState.Results(currentVacancies, totalCount!!))
                }

                isLoadingNextPage = false
            }
        }
    }

    private fun handleSearchError(isFirstPage: Boolean, errorMessage: String) {
        if (isFirstPage) {
            _searchScreenState.postValue(
                if (errorMessage == HttpsURLConnection.HTTP_BAD_REQUEST.toString()) {
                    SearchScreenState.ErrorFirstPage
                } else {
                    SearchScreenState.NoInternetFirstPage
                }
            )
        } else {
            _searchScreenState.postValue(
                if (errorMessage == HttpsURLConnection.HTTP_BAD_REQUEST.toString()) {
                    SearchScreenState.ErrorNextPage
                } else {
                    SearchScreenState.NoInternetNextPage
                }
            )
        }
        isLoadingNextPage = false
    }

    fun getNextPage() {
        if (!isEndOfListReached && !isLoadingNextPage) {
            isLoadingNextPage = true
            page++
            searchRequest()
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }
}
