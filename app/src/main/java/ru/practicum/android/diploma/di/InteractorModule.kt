package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.HhInteractor
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor
import ru.practicum.android.diploma.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.HhInteractorImpl

val interactorModule = module {
    factory<HhInteractor> {
        HhInteractorImpl(get())
    }
    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}
