package ru.practicum.android.diploma.util.extentions

import java.text.DecimalFormat

fun getFormattedSalary(salaryFrom: Int, salaryTo: Int, currency: String): String {
    val decimalFormat = DecimalFormat("#,###")
    val currencySymbol = getCurrencySymbol(currency)
    return when {
        salaryFrom > 0 && salaryTo > 0 -> {
            "от ${decimalFormat.format(salaryFrom)} до ${decimalFormat.format(salaryTo)} $currencySymbol"
        }
        salaryFrom > 0 -> {
            "от ${decimalFormat.format(salaryFrom)} $currencySymbol"
        }
        salaryTo > 0 -> {
            "до ${decimalFormat.format(salaryTo)} $currencySymbol"
        }
        else -> {
            "Зарплата не указана"
        }
    }
}
