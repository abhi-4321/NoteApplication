package com.abhinav.notesapplication.database

import android.content.Context
import android.database.DatabaseUtils
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abhinav.notesapplication.model.Note
import com.abhinav.notesapplication.model.User

@Database(entities = [Note::class, User::class], version = 1, exportSchema = false)
abstract class NotesDB : RoomDatabase() {
    abstract fun notesDao() : NotesDao
    companion object{
        @Volatile
        private var INSTANCE : NotesDB? = null

        fun getInstance(context: Context) : NotesDB {
            if (INSTANCE==null)
            {
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,
                        NotesDB::class.java,
                        "notes").build()
                }
            }
            return INSTANCE!!
        }
    }
}