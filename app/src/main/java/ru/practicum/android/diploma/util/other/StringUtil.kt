package ru.practicum.android.diploma.util.other

class StringUtil {
    companion object {
        fun countVacancies(count: Int): String {
            val string = when (count % 10) {
                1 -> " вакансия"
                2, 3, 4 -> " вакансии"
                else -> " вакансий"
            }
            return count.toString() + string
        }
    }
}

/*
-- Пример использования --
    val vacanciesCount = 23
    val vacanciesText = StringUtil.countVacancies(vacanciesCount)

    val vacanciesTextView = findViewById<TextView>(R.id.vacancies_text_view)
    vacanciesTextView.text = vacanciesText
 */
