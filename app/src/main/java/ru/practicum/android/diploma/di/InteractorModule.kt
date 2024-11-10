package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.ui.root.RootActivity

val interactorModule = module {
    factory<VacancyInteractor> {
        VacancyInteractorImpl(get())
    }
    scope<RootActivity> {
        scoped { VacancyInteractorImpl(get()) }
    }
}
