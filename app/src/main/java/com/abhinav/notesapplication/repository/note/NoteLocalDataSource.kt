package com.abhinav.notesapplication.repository.note

import com.abhinav.notesapplication.database.NotesDB
import com.abhinav.notesapplication.model.note.Note
import kotlinx.coroutines.flow.Flow

class NoteLocalDataSource  constructor(notesDB: NotesDB) {
    private val notesDao = notesDB.notesDao()

    suspend fun insert(note: Note) {
        notesDao.insertNote(note)
    }

    suspend fun update(note: Note) {
        notesDao.updateNote(note)
    }

    suspend fun delete(id: Int) {
        notesDao.deleteNote(id,"")
    }

    fun getNotes() : Flow<List<Note>> {
        return notesDao.getNotes("")
    }
}