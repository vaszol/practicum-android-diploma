package ru.practicum.android.diploma.util.extentions

import ru.practicum.android.diploma.data.dto.VacanciesRequest

fun getQueryMap(request: VacanciesRequest): HashMap<String, String> {
    val options: HashMap<String, String> = HashMap()
    options["text"] = request.text
    options["currency"] = request.currency
    if (!request.area.isNullOrEmpty()) {
        options["area"] = request.area
    }
    if (!request.industry.isNullOrEmpty()) {
        options["industry"] = request.industry
    }
    if (request.salary != null) {
        options["salary"] = request.salary.toString()
    }
    options["only_with_salary"] = request.onlyWithSalary.toString()
    options["per_page"] = request.size.toString()
    options["page"] = request.page.toString()
    options["locale"] = request.locale
    options["host"] = request.host
    return options
}
