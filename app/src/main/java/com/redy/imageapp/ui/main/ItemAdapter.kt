package com.redy.imageapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redy.imageapp.R
import java.util.ArrayList

class ItemAdapter(private val click: (ItemRow) -> Unit) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val items = ArrayList<ItemRow>()

    class ViewHolder(private val click: (ItemRow) -> Unit, itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)

        fun bind(row: ItemRow) {
            name.text = when(row.isHorizontal) {
                true -> itemView.context.getString(R.string.horizontal_image_name, row.name)
                else -> itemView.context.getString(R.string.vertical_image_name, row.name)
            }
            itemView.setOnClickListener { click.invoke(row) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_row, parent, false)

        return ViewHolder(click, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(newItems: List<ItemRow>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
