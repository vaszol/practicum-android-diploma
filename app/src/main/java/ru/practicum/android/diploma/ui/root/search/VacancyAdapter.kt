package ru.practicum.android.diploma.ui.root.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter(
     private val onPlaylistClickListener: OnPlaylistClickListener
) : RecyclerView.Adapter<VacancyViewHolder>() {
   var vacancies = mutableListOf<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val binding = ItemVacancyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancy = vacancies[position]
        holder.bind(vacancy)
        holder.itemView.setOnClickListener { onPlaylistClickListener.onPlaylistClick(vacancies.get(position)) }

    }

    override fun getItemCount(): Int = vacancies.size

    fun updateData(newVacancies: List<Vacancy>) {
        (vacancies as MutableList).clear()
        vacancies.addAll(newVacancies)
        notifyDataSetChanged()
    }

    fun interface OnPlaylistClickListener {
        fun onPlaylistClick(vacancy: Vacancy)
    }
}
