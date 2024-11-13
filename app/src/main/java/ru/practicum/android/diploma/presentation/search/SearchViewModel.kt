package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.debouncer.Debouncer
import kotlin.random.Random

class SearchViewModel : ViewModel() {
    private val _searchScreenState = MutableLiveData<SearchScreenState>()
    val searchScreenState: LiveData<SearchScreenState> = _searchScreenState
    private val vacancies = mutableListOf<Vacancy>()
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
        vacancies.add(
            Vacancy(
                "1",
                "Android разработчик",
                "Glovo",
                "-1",
                Area("1", "Москва", "0", emptyList()),
                "-1",
                "-1",
                "-1",
                100000,
                150000,
                "RUR",
                "Самая лучшая работа"
            )
        )
        vacancies.add(
            Vacancy(
                "1",
                "Курьер",
                "Glovo",
                "https://img.hhcdn.ru/employer-logo/6147645.jpeg",
                Area("1", "Москва", "0", emptyList()),
                "-1",
                "-1",
                "-1",
                -1,
                100000,
                "RUR",
                "Хорошая работа"
            )
        )
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                _searchScreenState.postValue(SearchScreenState.Loading)
                delay(500)
                when (Random.nextInt(3)) {
                    0 -> _searchScreenState.postValue(SearchScreenState.Results(vacancies))
                    1 -> _searchScreenState.postValue(SearchScreenState.NothingFound)
                    2 -> _searchScreenState.postValue(SearchScreenState.NoInternet)
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

}
