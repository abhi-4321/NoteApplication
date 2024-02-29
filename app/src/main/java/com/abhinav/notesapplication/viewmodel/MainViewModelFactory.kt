package com.abhinav.notesapplication.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abhinav.notesapplication.repository.auth.AuthRepository

class MainViewModelFactory(private val authRepository: AuthRepository, private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(authRepository,sharedPreferences) as T
    }
}