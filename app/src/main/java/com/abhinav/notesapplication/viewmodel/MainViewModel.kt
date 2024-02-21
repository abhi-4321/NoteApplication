package com.abhinav.notesapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinav.notesapplication.database.NotesDB
import com.abhinav.notesapplication.database.NotesDao
import com.abhinav.notesapplication.model.Note
import com.abhinav.notesapplication.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val notesDao: NotesDao) : ViewModel() {

    //Splash Screen
    private val mutableStateFlow = MutableStateFlow(true)
    val isLoading = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            mutableStateFlow.value = false
        }
    }

    //Room Operations
    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDao.insertUser(user)
        }
    }
    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDao.updateNote(note)
        }
    }
    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDao.insertNote(note)
        }
    }
    fun deleteNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDao.deleteNote(id)
        }
    }
    fun getNoteById(id: Int) : StateFlow<Note> = notesDao.getNoteById(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Note.emptyNote())
    fun getNotes() : StateFlow<List<Note>> = notesDao.getNotes().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

}