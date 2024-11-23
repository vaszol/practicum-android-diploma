package ru.practicum.android.diploma.data.sharedPreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.practicum.android.diploma.domain.SharedPreferencesConverter
import ru.practicum.android.diploma.domain.api.SharedPreferencesRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

class SharedPreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesConverter: SharedPreferencesConverter,
) : SharedPreferencesRepository {
    override fun setCountry(country: Area) {
        val countryString = sharedPreferencesConverter.convertAreaToJson(country)
        sharedPreferences.edit { putString(COUNTRY_KEY, countryString) }
    }

    override fun getCountry(): Area? {
        return sharedPreferences.getString(COUNTRY_KEY, null)?.let {
            sharedPreferencesConverter.convertJsonToArea(it)
        }
    }

    override fun setRegion(region: Area) {
        val regionString = sharedPreferencesConverter.convertAreaToJson(region)
        sharedPreferences.edit { putString(REGION_KEY, regionString) }
    }

    override fun getRegion(): Area? {
        return sharedPreferences.getString(REGION_KEY, null)?.let {
            sharedPreferencesConverter.convertJsonToArea(it)
        }
    }

    override fun addIndustry(industry: Industry) {
        val list = getIndustries()
        list.add(industry)
        val json = sharedPreferencesConverter.convertListToJson(list)
        sharedPreferences.edit { putString(INDUSTRY_KEY, json) }
    }

    override fun removeIndustry(industry: Industry) {
        val list = getIndustries()
        list.remove(industry)
        val json = sharedPreferencesConverter.convertListToJson(list)
        sharedPreferences.edit { putString(INDUSTRY_KEY, json) }
    }

    override fun getIndustries(): MutableList<Industry> {
        return sharedPreferences.getString(INDUSTRY_KEY, null)?.let {
            sharedPreferencesConverter.convertJsonToList(it)
        }?.toMutableList() ?: mutableListOf()
    }

    override fun clearIndustries() {
        sharedPreferences.edit { putString(INDUSTRY_KEY, null) }
    }

    override fun setSalary(salary: Int) {
        sharedPreferences.edit { putString(SALARY_KEY, salary.toString()) }
    }

    override fun getSalary(): Int? {
        return sharedPreferences.getString(SALARY_KEY, null)?.toInt()
    }

    override fun setShowOnlyWithSalary(showOnlyWithSalary: Boolean) {
        sharedPreferences.edit { putBoolean(SHOW_ONLY_WITH_SALARY, showOnlyWithSalary) }
    }

    override fun getShowOnlyWithSalary(): Boolean {
        return sharedPreferences.getBoolean(SHOW_ONLY_WITH_SALARY, false)
    }

    companion object {
        private const val COUNTRY_KEY = "country_key"
        private const val REGION_KEY = "region_key"
        private const val INDUSTRY_KEY = "industry_key"
        private const val SALARY_KEY = "salary_key"
        private const val SHOW_ONLY_WITH_SALARY = "show_only_with_salary"
    }
}
