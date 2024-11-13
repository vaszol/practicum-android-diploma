package ru.practicum.android.diploma.ui.root.search

import ru.practicum.android.diploma.domain.models.Vacancy

data class SearchScreenState(
    val vacancies: List<Vacancy> = listOf(),
    val show: Show = Show.BINOCULARS, //статус картинки
)

enum class Show {
    BINOCULARS,     //показывать бинокль
    TROBER,         //показывать тробер
    FAIL,          //показывать череп
    LIST,           //показывать список
    EMPTY             //показывать кота
}
