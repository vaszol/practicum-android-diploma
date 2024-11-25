package ru.practicum.android.diploma.presentation.filter.industry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.filter.industry.IndustryScreenState.Content
import java.io.IOException

class IndustryViewModel(
    private val hhInteractor: HhInteractor,
    private val sharedPrefInteractor: SharedPreferencesInteractor
) : ViewModel() {
    private val _industryScreenState = MutableLiveData<IndustryScreenState>()
    val industryScreenState: LiveData<IndustryScreenState> = _industryScreenState

    private val _selectedIndustry = MutableLiveData<Industry?>()
    val selectedIndustry: LiveData<Industry?> get() = _selectedIndustry

    fun getIndustries() {
        _industryScreenState.value = IndustryScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val industries = hhInteractor.getIndustries().firstOrNull() ?: emptyList()
                _industryScreenState.postValue(Content(industries))

                val storedIndustry = sharedPrefInteractor.getIndustry()
                storedIndustry?.let {
                    _selectedIndustry.postValue(it)
                }
            } catch (e: IOException) {
                Log.e("Exception caught in IndustryViewModel", "IOException occurred: ${e.localizedMessage}", e)
                _industryScreenState.postValue(IndustryScreenState.Error)
            } catch (e: JsonSyntaxException) {
                Log.e("Exception caught in IndustryViewModel", "JsonSyntaxException occurred: ${e.localizedMessage}", e)
                _industryScreenState.postValue(IndustryScreenState.Error)
            }
        }
    }

    fun selectIndustry(industry: Industry?) {
        _selectedIndustry.value = industry
    }

    fun saveSelectedIndustry(industry: Industry) {
        sharedPrefInteractor.setIndustry(industry)
    }
}
