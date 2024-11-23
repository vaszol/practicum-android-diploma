package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Industry

class SharedPreferencesInteractorImpl(
    private val repo: SharedPreferencesRepository,
) : SharedPreferencesInteractor {
    override fun setCountry(country: Area) {
        repo.setCountry(country)
    }

    override fun getCountry(): Area? {
        return repo.getCountry()
    }

    override fun setRegion(region: Area) {
        repo.setRegion(region)
    }

    override fun getRegion(): Area? {
        return repo.getRegion()
    }

    override fun addIndustry(industry: Industry) {
        repo.addIndustry(industry)
    }

    override fun removeIndustry(industry: Industry) {
        repo.removeIndustry(industry)
    }

    override fun getIndustries(): MutableList<Industry> {
        return repo.getIndustries()
    }

    override fun setSalary(salary: Int) {
        repo.setSalary(salary)
    }

    override fun getSalary(): Int? {
        return repo.getSalary()
    }

    override fun setShowOnlyWithSalary(isShowOnlyWithSalary: Boolean) {
        repo.setShowOnlyWithSalary(isShowOnlyWithSalary)
    }

    override fun getShowOnlyWithSalary(): Boolean {
        return repo.getShowOnlyWithSalary()
    }
}
