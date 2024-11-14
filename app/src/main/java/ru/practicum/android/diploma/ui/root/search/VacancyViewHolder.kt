package ru.practicum.android.diploma.ui.root.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.Vacancy.Companion.VACANCY_DEFAULT_STRING_VALUE
import ru.practicum.android.diploma.util.extentions.formatSalary

class VacancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemVacancyBinding.bind(itemView)

    fun bind(model: Vacancy) {
        with(binding) {
            vacancyTitle.text = "${model.name}, ${model.area?.name}"
            employerName.text = model.employerName
            salary.text = model.formatSalary()
            if (model.employerLogoUrl90 != VACANCY_DEFAULT_STRING_VALUE) {
                Glide.with(companyLogo.context)
                    .load(model.employerLogoUrl90)
                    .placeholder(R.drawable.employer_logo_placeholder)
                    .into(companyLogo)
            } else {
                companyLogo.setImageResource(R.drawable.employer_logo_placeholder)
            }
        }
    }
}
