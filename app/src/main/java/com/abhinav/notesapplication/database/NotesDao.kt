package com.abhinav.notesapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.abhinav.notesapplication.model.note.Note
import com.abhinav.notesapplication.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Upsert
    suspend fun upsertUser(user: User)

    @Query("Select * from notes " +
            "where id=:id and userId=:uId")
    fun getNoteById(id: Int, uId: String) : Flow<Note>

    @Query("Delete from notes " +
            "where id=:id and userId=:uId")
    suspend fun deleteNote(id: Int, uId: String)

    @Transaction
    @Query("Select * from notes " +
            "where userId=:uId")
    fun getNotes(uId: String) : Flow<List<Note>>

}