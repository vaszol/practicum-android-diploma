package ru.practicum.android.diploma.presentation.filter.industry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.filter.industry.IndustryScreenState.Content

class IndustryViewModel(
    private val hhInteractor: HhInteractor,
    private val sharedPrefInteractor: SharedPreferencesInteractor
) : ViewModel() {
    private val _industryScreenState = MutableLiveData<IndustryScreenState>()
    val industryScreenState: LiveData<IndustryScreenState> = _industryScreenState

    private val _selectedIndustry = MutableLiveData<Industry?>()
    val selectedIndustry: LiveData<Industry?>
        get() = _selectedIndustry

    private var originalList: List<Industry> = emptyList()

    fun getIndustries() {
        _industryScreenState.value = IndustryScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            hhInteractor.getIndustries()
                .collect { pair ->
                    when {
                        pair.second != null -> {
                            Log.e("IOException caught in IndustryViewModel", "IOException occurred")
                            _industryScreenState.postValue(IndustryScreenState.NoInternet)
                        }

                        pair.first.isNullOrEmpty() -> {
                            Log.e("Exception caught in IndustryViewModel", "industries.isNullOrEmpty")
                            _industryScreenState.postValue(IndustryScreenState.Error)
                        }

                        else -> {
                            originalList = pair.first!!
                            _industryScreenState.postValue(Content(pair.first!!))
                        }
                    }
                }
            try {
                val storedIndustry = sharedPrefInteractor.getIndustry()
                storedIndustry?.let {
                    _selectedIndustry.postValue(it)
                }
            } catch (e: JsonSyntaxException) {
                Log.e("JsonSyntaxException caught in IndustryViewModel", e.localizedMessage, e)
                _industryScreenState.postValue(IndustryScreenState.Error)
            }
        }
    }

    fun selectIndustry(industry: Industry?) {
        _selectedIndustry.value = industry
    }

    fun filter(query: String) {
        viewModelScope.launch {
            val filteredList = if (query.isEmpty()) {
                originalList
            } else {
                originalList.filter { it.name.contains(query, ignoreCase = true) }
            }

            _industryScreenState.value = if (filteredList.isEmpty()) {
                IndustryScreenState.NoResults
            } else {
                Content(filteredList)
            }
        }
    }
}
