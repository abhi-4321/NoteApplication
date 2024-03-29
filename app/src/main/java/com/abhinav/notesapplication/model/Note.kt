package com.abhinav.notesapplication.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: String,
    val title: String,
    val content: String,
    val date: String
)
{
    companion object {
        fun emptyNote() : Note = Note(-1,"","","","")
    }
}
