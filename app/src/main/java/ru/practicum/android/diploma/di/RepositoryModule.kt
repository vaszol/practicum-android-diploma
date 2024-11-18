package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.FavoriteRepositoryImpl
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.converter.VacancyConverter
import ru.practicum.android.diploma.data.details.api.DetailsVacancyRepository
import ru.practicum.android.diploma.data.details.impl.DetailsVacancyRepositoryImpl
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository

val repositoryModule = module {
    factory { VacancyConverter() }
    single<VacancyRepository> {
        VacanciesRepositoryImpl(get(), get())
    }
    factory<FavoriteRepository> {
        FavoriteRepositoryImpl(get())
    }
    single<DetailsVacancyRepository> {
        DetailsVacancyRepositoryImpl(get(), get())
    }
}
