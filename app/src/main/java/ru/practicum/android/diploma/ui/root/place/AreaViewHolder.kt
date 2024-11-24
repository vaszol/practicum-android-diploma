package ru.practicum.android.diploma.ui.root.place

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Area

class AreaViewHolder(itemView: View) : ViewHolder(itemView) {
    private val item = itemView.findViewById<Button>(R.id.item_area)

    fun bind(area: Area) {
        item.text = area.name
    }
}
