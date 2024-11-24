package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedFilterViewModel : ViewModel() {
    private val _salary = MutableLiveData<Int?>()
    val salary: LiveData<Int?> = _salary

    private val _showOnlyWithSalary = MutableLiveData<Boolean>()
    val showOnlyWithSalary: LiveData<Boolean> = _showOnlyWithSalary

    fun setFilters(salary: Int?, showOnlyWithSalary: Boolean) {
        _salary.value = salary
        _showOnlyWithSalary.value = showOnlyWithSalary
    }
}
