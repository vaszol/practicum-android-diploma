package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.details.DetailsViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.favorite.FavoriteViewModel

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::FavoriteViewModel)
    viewModel{params ->
        DetailsViewModel(id = params.get(), get())
    }
}
