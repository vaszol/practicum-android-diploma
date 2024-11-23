package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.FavoriteRepositoryImpl
import ru.practicum.android.diploma.data.HhRepositoryImpl
import ru.practicum.android.diploma.data.sharedPreferences.SharedPreferencesRepositoryImpl
import ru.practicum.android.diploma.data.converter.VacancyConverter
import ru.practicum.android.diploma.domain.api.HhRepository
import ru.practicum.android.diploma.domain.api.SharedPreferencesRepository
import ru.practicum.android.diploma.domain.favorite.FavoriteRepository

val repositoryModule = module {
    factory { VacancyConverter() }
    single<HhRepository> {
        HhRepositoryImpl(get(), get(), get())
    }
    factory<FavoriteRepository> {
        FavoriteRepositoryImpl(get())
    }
    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(get(), get())
    }
}
