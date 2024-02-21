package com.abhinav.notesapplication.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abhinav.notesapplication.databinding.NoteBinding
import com.abhinav.notesapplication.model.Note

class NotesListAdapter(private val adapterClickListener: AdapterClickListener) : ListAdapter<Note, NotesListAdapter.ViewHolder>(DiffUtilCallback()) {

    inner class ViewHolder(private val binding: NoteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(note: Note) {
            binding.date.text = note.date
            binding.title.text = note.title
            binding.content.text = note.content
            binding.delete.setOnClickListener { adapterClickListener.onDeleteClick(note.id) }
            binding.edit.setOnClickListener { adapterClickListener.onEditClick(note.id) }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}