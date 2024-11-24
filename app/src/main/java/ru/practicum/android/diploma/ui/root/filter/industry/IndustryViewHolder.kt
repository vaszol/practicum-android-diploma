package ru.practicum.android.diploma.ui.root.filter.industry

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryViewHolder(private val binding: ItemIndustryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(industry: Industry, isSelected: Boolean) {
        binding.nameIndustry.text = industry.name
        val checkButtonResource = if (isSelected) {
            R.drawable.ic_industry_button_on
        } else {
            R.drawable.ic_industry_button_off
        }
        binding.checkIndustryButton.setImageResource(checkButtonResource)
    }
}
