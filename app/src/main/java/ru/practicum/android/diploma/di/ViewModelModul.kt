package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.details.DetailsViewModel
import ru.practicum.android.diploma.presentation.favorite.FavoriteViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.presentation.place.SelectCountryViewModel
import ru.practicum.android.diploma.presentation.place.SelectRegionViewModel
import ru.practicum.android.diploma.presentation.place.SelectPlaceViewModel
import ru.practicum.android.diploma.presentation.filter.industry.IndustryViewModel

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::SelectCountryViewModel)
    viewModelOf(::SelectRegionViewModel)
    viewModelOf(::SelectPlaceViewModel)
    viewModelOf(::IndustryViewModel)
    viewModelOf(::FilterViewModel)
}
