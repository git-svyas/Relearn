package com.example.mobileapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mobileapp.model.Notes

class NotesDiffUtil(private val oldList: List<Notes>,
                        private val newList: List<Notes>
                        ) : DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }
}