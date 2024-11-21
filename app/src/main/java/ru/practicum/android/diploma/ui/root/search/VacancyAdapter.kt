package ru.practicum.android.diploma.ui.root.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(
    val vacancies: List<Vacancy>,
    private val clickListener: VacancyClickListener,
) : RecyclerView.Adapter<VacancyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancy = vacancies[position]
        holder.bind(vacancy)
        holder.itemView.setOnClickListener { clickListener.onVacancyClick(vacancies.get(position)) }
    }

    override fun getItemCount(): Int = vacancies.size

    fun updateData(newVacancies: List<Vacancy>) {
        (vacancies as MutableList).clear()
        vacancies.addAll(newVacancies)
        notifyDataSetChanged()
    }

    fun interface VacancyClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }
}
