package ru.practicum.android.diploma.util.extentions

fun getCurrencySymbol(code: String): String {
    val currencyMap = mapOf(
        "AZN" to "₼",
        "BYR" to "Br",
        "EUR" to "€",
        "GEL" to "₾",
        "KGS" to "KGT",
        "KZT" to "₸",
        "RUR" to "₽",
        "UAH" to "₴",
        "USD" to "\$",
        "UZS" to "UZS"
    )
    return currencyMap[code] ?: "Неизвестная валюта"
}
