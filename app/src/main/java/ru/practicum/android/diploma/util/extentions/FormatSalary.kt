package ru.practicum.android.diploma.util.extentions

import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.DecimalFormat

fun Vacancy.formatSalary(): String {
    val decimalFormat = DecimalFormat("#,###")
    return when {
        salaryFrom > 0 && salaryTo > 0 -> {
            "от ${decimalFormat.format(salaryFrom)} до ${decimalFormat.format(salaryTo)} ₽"
        }
        salaryFrom > 0 -> {
            "от ${decimalFormat.format(salaryFrom)} ₽"
        }
        salaryTo > 0 -> {
            "до ${decimalFormat.format(salaryTo)} ₽"
        }
        else -> {
            "Зарплата не указана"
        }
    }
}
