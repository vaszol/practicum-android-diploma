package ru.practicum.android.diploma.ui.root.filter.industry

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryAdapter(
    private val onItemSelected: (Industry) -> Unit
) : RecyclerView.Adapter<IndustryViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION
    private var originalList: List<Industry> = emptyList()
    private var currentList: MutableList<Industry> = mutableListOf()
    private var selectedIndustry: Industry? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIndustryBinding.inflate(inflater, parent, false)
        return IndustryViewHolder(binding)
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: IndustryViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(currentList[position], position == selectedPosition)

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onItemSelected(currentList[position])
        }
    }

    fun updateList(newList: List<Industry>) {
        originalList = newList
        currentList.clear()
        currentList.addAll(newList)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        currentList = if (query.isEmpty()) {
            originalList.toMutableList()
        } else {
            originalList.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun setSelectedIndustry(industry: Industry?) {
        selectedIndustry = industry
        selectedPosition = currentList.indexOfFirst { it == industry }
        notifyDataSetChanged()
    }
}
