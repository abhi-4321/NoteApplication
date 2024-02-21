package com.abhinav.notesapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.abhinav.notesapplication.model.Note
import com.abhinav.notesapplication.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Insert
    suspend fun insertUser(user: User)

    @Query("Select * from notes " +
            "where id=:id")
    fun getNoteById(id: Int) : Flow<Note>

    @Query("Delete from notes " +
            "where id=:id")
    suspend fun deleteNote(id: Int)

    @Query("Select * from notes")
    fun getNotes() : Flow<List<Note>>

}