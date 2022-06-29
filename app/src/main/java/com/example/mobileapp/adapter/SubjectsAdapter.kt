package com.example.mobileapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.model.Subject

class SubjectsAdapter(var context: Context, var arrayList: ArrayList<Subject>, val listener: OnItemClickListener) :
    RecyclerView.Adapter<SubjectsAdapter.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.subject_item, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val subject: Subject = arrayList.get(position)
        holder.icons.setImageResource(subject.Imageicon!!)
        holder.titles.text = subject.name
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icons = itemView.findViewById<ImageView>(R.id.icon_image_view)
        var titles = itemView.findViewById<TextView>(R.id.title_text_view)

        init{
            itemView.setOnClickListener{
                listener.onClick(adapterPosition)
            }
        }

    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

}