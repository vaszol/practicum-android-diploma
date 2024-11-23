package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.api.SharedPreferencesInteractor
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor
import ru.practicum.android.diploma.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.HhInteractorImpl
import ru.practicum.android.diploma.domain.impl.SharedPreferencesInteractorImpl

val interactorModule = module {
    factory<HhInteractor> {
        HhInteractorImpl(get())
    }
    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
    single<SharedPreferencesInteractor>(createdAtStart = true) {
        SharedPreferencesInteractorImpl(get())
    }
}
