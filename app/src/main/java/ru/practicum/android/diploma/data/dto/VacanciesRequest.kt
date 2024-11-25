package ru.practicum.android.diploma.data.dto

data class VacanciesRequest(
    val text: String,
    val currency: String,
    val area: String?,
    val industry: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
    val size: Int,
    val page: Int,
    val locale: String = "RU",
    val host: String = "hh.ru"
) {
    fun getQueryMap(): HashMap<String, String> {
        val options: HashMap<String, String> = HashMap()
        options["text"] = text
        options["currency"] = currency
        if (!area.isNullOrEmpty()) {
            options["area"] = area
        }
        if (!industry.isNullOrEmpty()) {
            options["industry"] = industry
        }
        if (salary != null) {
            options["salary"] = salary.toString()
        }
        options["only_with_salary"] = onlyWithSalary.toString()
        options["per_page"] = size.toString()
        options["page"] = page.toString()
        options["locale"] = locale
        options["host"] = host
        return options
    }
}

