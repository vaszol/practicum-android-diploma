package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.search.SearchViewModel

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
}
