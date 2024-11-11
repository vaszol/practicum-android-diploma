package ru.practicum.android.diploma.data.db.converter

import ru.practicum.android.diploma.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.domain.models.Vacancy

class FavoriteVacancyDbConverter {
    fun map(vacancy: Vacancy): FavoriteVacancyEntity {
        return FavoriteVacancyEntity(
            vacancy.id,
            vacancy.name,
            vacancy.employerName,
            vacancy.employerLogoUrl90,
            vacancy.area,
            vacancy.experience,
            vacancy.employment,
            vacancy.schedule,
            vacancy.salaryFrom,
            vacancy.salaryTo,
            vacancy.currency,
            vacancy.description
        )
    }

    fun map(vacancy: FavoriteVacancyEntity): Vacancy {
        return Vacancy(
            vacancy.id,
            vacancy.name,
            vacancy.employerName,
            vacancy.employerLogoUrl90,
            vacancy.area,
            vacancy.experience,
            vacancy.employment,
            vacancy.schedule,
            vacancy.salaryFrom,
            vacancy.salaryTo,
            vacancy.currency,
            vacancy.description
        )
    }
}
