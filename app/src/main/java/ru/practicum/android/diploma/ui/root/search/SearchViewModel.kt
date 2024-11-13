package ru.practicum.android.diploma.ui.root.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.ui.root.SingleLiveEvent
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor,
) : ViewModel() {

    private val searchDebounce =
        debounce<Unit>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { _ ->
            search()
        }

    private var searchText: String = ""
    private var page: Int = 0
    private val _state = MutableLiveData<SearchScreenState>()
    val state: LiveData<SearchScreenState> = _state
    val event = SingleLiveEvent<SearchScreenEvent>()
    fun doAfterTextChanged(text: String) {
        searchText = text
    }

    fun search() {
        if (searchText.isNotEmpty()) {
            goneImage()
            _state.postValue(SearchScreenState(show = Show.TROBER, vacancies = mutableListOf()))

            viewModelScope.launch(Dispatchers.IO) {
                vacancyInteractor.searchVacancies(searchText, Vacancy.CURRENCY_DEFAULT_VALUE, page)
                    .collect { pair ->
                        if (pair.second != null) {
                            messageFail()
                        } else if (pair.first.isNullOrEmpty()) {
                            messageEmpty()
                        } else {
                            messageOk(pair.first!!)
                        }
                    }
            }
        }
    }

    private fun messageOk(list: List<Vacancy>) {
        _state.postValue(
            SearchScreenState(
                vacancies = list,
                show = Show.LIST
            )
        )
    }

    private fun messageEmpty() {
        _state.postValue(
            SearchScreenState(
                vacancies = emptyList(),
                show = Show.EMPTY
            )
        )
    }

    private fun messageFail() {
        _state.postValue(
            SearchScreenState(
                vacancies = emptyList(),
                show = Show.FAIL
            )
        )
    }

    fun focusChange(hasFocus: Boolean) {
        if (hasFocus && searchText.isEmpty()) showImage() else goneImage()
    }

    fun searchDebounce() = searchDebounce(Unit)

    fun showImage() {
        _state.postValue(
            SearchScreenState(
                vacancies = emptyList(),
                show = Show.BINOCULARS
            )
        )
    }

    fun goneImage() {
        _state.postValue(
            SearchScreenState(
                vacancies = emptyList(),
                show = Show.TROBER
            )
        )
    }

    fun clearText() {
        event.value = SearchScreenEvent.HideKeyboard
        event.value = SearchScreenEvent.ClearSearch
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
