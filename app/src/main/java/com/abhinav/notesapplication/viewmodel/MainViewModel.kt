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
    fun upsertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDao.upsertUser(user)
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
    fun deleteNote(id: Int, uId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notesDao.deleteNote(id, uId)
        }
    }
    fun getNoteById(id: Int, uId: String) : StateFlow<Note> = notesDao.getNoteById(id, uId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Note.emptyNote())
    fun getNotes(uId: String) : StateFlow<List<Note>> = notesDao.getNotes(uId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

}