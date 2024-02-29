package com.abhinav.notesapplication.repository.note

import com.abhinav.notesapplication.api.NoteApi
import com.abhinav.notesapplication.model.note.Note
import java.io.IOException

class NoteRemoteDataSource  constructor(val notesApi: NoteApi) {
    suspend fun insert(note: Note) : Boolean {
        val result = notesApi.insert(note)
        return result.isSuccessful
    }

    suspend fun update(note: Note) : Boolean {
        val result = notesApi.update(note)
        return result.isSuccessful
    }

    suspend fun delete(id: Int) : Boolean {
        val result = notesApi.delete(id)
        return result.isSuccessful
    }

    fun getNotes() : Result<List<Note>> {
        return Result.failure(IOException("No Data Received"))
    }
}