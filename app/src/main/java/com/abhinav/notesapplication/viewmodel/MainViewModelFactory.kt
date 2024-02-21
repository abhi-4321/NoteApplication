package com.abhinav.notesapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abhinav.notesapplication.database.NotesDao

class MainViewModelFactory(private val notesDao: NotesDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(notesDao) as T
    }
}