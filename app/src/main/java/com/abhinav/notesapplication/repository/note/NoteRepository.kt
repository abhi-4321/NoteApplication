package com.abhinav.notesapplication.repository.note

import com.abhinav.notesapplication.model.note.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NoteRepository {
    suspend fun insert(note: Note) : Boolean
    suspend fun update(note: Note) : Boolean
    suspend fun delete(id: Int) : Boolean
    fun getNotes() : Flow<List<Note>>
}