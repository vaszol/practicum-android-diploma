package ru.practicum.android.diploma.ui.root.filter.industry

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryAdapter(
    private val onItemSelected: (Industry?) -> Unit
) : RecyclerView.Adapter<IndustryViewHolder>() {
    private var currentList: MutableList<Industry> = mutableListOf()
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    private var selectedIndustry: Industry? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIndustryBinding.inflate(inflater, parent, false)
        return IndustryViewHolder(binding)
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: IndustryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val industry = currentList[position]
        holder.bind(industry, industry == selectedIndustry)

        holder.itemView.setOnClickListener {
            if (selectedIndustry == industry) {
                // Если элемент уже выбран, сбрасываем выбор
                val previousPosition = selectedPosition
                selectedPosition = RecyclerView.NO_POSITION
                selectedIndustry = null
                notifyItemChanged(previousPosition)
                onItemSelected(null) // Передаем null как снятие выбора
            } else {
                // Если элемент не выбран, устанавливаем его
                val previousPosition = selectedPosition
                selectedPosition = position
                selectedIndustry = industry
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemSelected(industry)
            }
        }
    }

    fun updateList(newList: List<Industry>) {
        currentList.clear()
        currentList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setSelectedIndustry(industry: Industry?) {
        selectedIndustry = industry
        selectedPosition = currentList.indexOfFirst { it == industry }
        notifyDataSetChanged()
    }
}
