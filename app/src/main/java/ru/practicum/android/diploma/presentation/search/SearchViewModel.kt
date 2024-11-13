package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.util.debouncer.Debouncer

class SearchViewModel : ViewModel() {
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
            viewModelScope.launch {
                _searchScreenState.postValue(SearchScreenState.Loading)
                delay(SEARCH_DEBOUNCE_DELAY)
                _searchScreenState.postValue(SearchScreenState.NothingFound)
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

}
