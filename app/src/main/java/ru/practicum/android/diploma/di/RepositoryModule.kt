package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.converter.VacancyConverter
import ru.practicum.android.diploma.domain.api.VacancyRepository

val repositoryModule = module {
    factory { VacancyConverter() }
    single<VacancyRepository> {
        VacanciesRepositoryImpl(get(), get())
    }
}
