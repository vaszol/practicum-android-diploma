package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

interface SharedPreferencesInteractor {
    fun setCountry(country: Area?)
    fun getCountry(): Area?
    fun setRegion(region: Area?)
    fun getRegion(): Area?
    fun setIndustry(industries: MutableList<Industry>)
    fun getIndustries(): MutableList<Industry>
    fun setSalary(salary: Int)
    fun getSalary(): Int?
    fun setShowOnlyWithSalary(isShowOnlyWithSalary: Boolean)
    fun getShowOnlyWithSalary(): Boolean
}
