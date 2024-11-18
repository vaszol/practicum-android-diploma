package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.DetailsVacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.favorite.FavoriteInteractor
import ru.practicum.android.diploma.domain.impl.DetailsVacancyInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.ui.root.RootActivity

val interactorModule = module {
    factory<VacancyInteractor> {
        VacancyInteractorImpl(get())
    }
    scope<RootActivity> {
        scoped { VacancyInteractorImpl(get()) }
    }
    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
    single<DetailsVacancyInteractor> {
        DetailsVacancyInteractorImpl(
            vacancyDetailsRepository = get()
        )
    }
}
