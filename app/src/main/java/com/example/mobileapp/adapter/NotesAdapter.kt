package com.example.mobileapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.model.Notes

class NotesAdapter(val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var notesList = ArrayList<Notes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notes_item,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is NotesViewHolder -> {
                holder.bind(notesList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun submitList(list: ArrayList<Notes>){
        val diffUtil = NotesDiffUtil(notesList,list)
        val diffResult = DiffUtil.calculateDiff(diffUtil);
        notesList = list
        diffResult.dispatchUpdatesTo(this)
//        notesList = list
//        notifyDataSetChanged()
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.tv_name)
        val views = itemView.findViewById<TextView>(R.id.tv_views)

        init{
            itemView.setOnClickListener{
                listener.onClick(adapterPosition)
            }
        }
        fun bind(note: Notes){
            name.setText(note.name)
            views.setText(note.views.toString())
        }
    }



    interface OnItemClickListener{
        fun onClick(position: Int)
    }
}