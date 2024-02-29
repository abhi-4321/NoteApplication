package com.abhinav.notesapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abhinav.notesapplication.model.note.Note
import com.abhinav.notesapplication.model.User

@Database(entities = [Note::class, User::class], version = 1, exportSchema = false)
abstract class NotesDB : RoomDatabase() {
    abstract fun notesDao() : NotesDao
}