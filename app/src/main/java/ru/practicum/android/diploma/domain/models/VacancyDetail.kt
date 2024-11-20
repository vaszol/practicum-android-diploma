package ru.practicum.android.diploma.domain.models

data class VacancyDetail(
    val id: String,
    val name: String,
    val employerName: String,
    val employerLogoUrl90: String, // URL логотипа (90x90)
    val area: Area, // Регион
    val experience: String, // Опыт работы
    val employment: String, // Тип занятости
    val schedule: String, // График работы
    val salaryFrom: Int, // Нижний предел зарплаты
    val salaryTo: Int, // Верхний предел зарплаты
    val currency: String, // Код валюты
    val description: String, // Описание вакансии в формате HTML
    val keySkills: List<String>, // Ключевые навыки
    val street: String, // Адрес (улица)
    val building: String, // Адрес (номер дома)
    val url: String?, // ссылка на вакансию
    val contactsEmail: String?, // контакты email
    val contactsName: String?, // контакты name
    val isFavorite: Boolean, // Вакансия в избранном
)
