package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

interface SharedPreferencesRepository {
    fun setCountry(country: Area)
    fun getCountry(): Area?
    fun setRegion(region: Area)
    fun getRegion(): Area?
    fun addIndustry(industry: Industry)
    fun removeIndustry(industry: Industry)
    fun getIndustries(): MutableList<Industry>
    fun setSalary(salary: Int)
    fun getSalary(): Int?
    fun setShowOnlyWithSalary(showOnlyWithSalary: Boolean)
    fun getShowOnlyWithSalary(): Boolean
}
