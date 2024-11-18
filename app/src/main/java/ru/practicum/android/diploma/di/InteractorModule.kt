package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor
import ru.practicum.android.diploma.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.ui.root.RootActivity

val interactorModule = module {
    factory<VacancyInteractor> {
        VacancyInteractorImpl(get())
    }
    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}
