package ru.practicum.android.diploma.ui.root.filter.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Area

class AreaAdapter(private val clickListener: (Area) -> Unit) :
    RecyclerView.Adapter<AreaViewHolder>() {

    var areas = ArrayList<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_area, parent, false)
        return AreaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return areas.size
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        holder.bind(areas[position])
        holder.itemView.setOnClickListener {
            clickListener(areas[position])
        }
    }
}
