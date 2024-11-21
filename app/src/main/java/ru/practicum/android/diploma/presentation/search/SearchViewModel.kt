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
import ru.practicum.android.diploma.ui.root.SingleLiveEvent
import ru.practicum.android.diploma.util.debouncer.Debouncer
import javax.net.ssl.HttpsURLConnection

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor,
) : ViewModel() {
    private var page: Int = 0
    private val _searchScreenState = MutableLiveData<SearchScreenState>()
    val searchScreenState: LiveData<SearchScreenState> = _searchScreenState
    val event = SingleLiveEvent<SearchEventState>()
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
        currentVacancies.clear()
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
            ).collect { triple ->
                if (triple.second != null) {
                    handleSearchError(triple.second!!)
                } else if (triple.first.isNullOrEmpty()) {
                    isEndOfListReached = true
                    if (page > 0) {
                        _searchScreenState.postValue(SearchScreenState.EndOfListReached)
                        event.postValue(SearchEventState.EndOfListReached)
                    } else {
                        _searchScreenState.postValue(SearchScreenState.NothingFound)
                    }
                } else {
                    page++
                    currentVacancies.addAll(triple.first!!)
                    _searchScreenState.postValue(SearchScreenState.Results(currentVacancies.distinct(), triple.third!!))
                }
                isLoadingNextPage = false
            }
        }
    }

    private fun handleSearchError(errorMessage: String) {
        if (page == 0) {
            _searchScreenState.postValue(
                if (errorMessage == HttpsURLConnection.HTTP_BAD_REQUEST.toString()) {
                    SearchScreenState.ErrorFirstPage
                } else {
                    SearchScreenState.NoInternetFirstPage
                }
            )
        } else {
                if (errorMessage == HttpsURLConnection.HTTP_BAD_REQUEST.toString()) {
                    _searchScreenState.postValue(SearchScreenState.ErrorNextPage)
                    event.postValue(SearchEventState.ErrorNextPage)
                } else {
                    _searchScreenState.postValue(SearchScreenState.NoInternetNextPage)
                    event.postValue(SearchEventState.NoInternetNextPage)
                }
        }
        isLoadingNextPage = false
    }

    fun getNextPage() {
        if (!isEndOfListReached && !isLoadingNextPage) {
            isLoadingNextPage = true
            searchRequest()
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }
}
