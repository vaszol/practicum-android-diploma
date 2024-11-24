package ru.practicum.android.diploma.data.shared

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
    override fun setCountry(country: Area?) {
        if (country != null) {
            val countryString = sharedPreferencesConverter.convertAreaToJson(country)
            sharedPreferences.edit { putString(COUNTRY_KEY, countryString) }
        } else {
            sharedPreferences.edit().remove(COUNTRY_KEY).apply()
        }
    }

    override fun getCountry(): Area? {
        return sharedPreferences.getString(COUNTRY_KEY, null)?.let {
            sharedPreferencesConverter.convertJsonToArea(it)
        }
    }

    override fun setRegion(region: Area?) {
        if (region != null) {
            val regionString = sharedPreferencesConverter.convertAreaToJson(region)
            sharedPreferences.edit { putString(REGION_KEY, regionString) }
        } else {
            sharedPreferences.edit().remove(REGION_KEY).apply()
        }
    }

    override fun getRegion(): Area? {
        return sharedPreferences.getString(REGION_KEY, null)?.let {
            sharedPreferencesConverter.convertJsonToArea(it)
        }
    }

    override fun setIndustry(industry: Industry?) {
        if (industry != null) {
            val json = sharedPreferencesConverter.convertIndustryToJson(industry)
            sharedPreferences.edit { putString(INDUSTRY_KEY, json) }
        } else {
            sharedPreferences.edit().remove(INDUSTRY_KEY).apply()
        }
    }

    override fun getIndustry(): Industry? {
        return sharedPreferences.getString(INDUSTRY_KEY, null)?.let {
            sharedPreferencesConverter.convertJsonToIndustry(it)
        }
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
