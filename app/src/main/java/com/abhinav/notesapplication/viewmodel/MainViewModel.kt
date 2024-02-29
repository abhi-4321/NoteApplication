package com.abhinav.notesapplication.viewmodel

import android.content.SharedPreferences
import android.media.session.MediaSession.Token
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinav.notesapplication.model.auth.AuthRequest
import com.abhinav.notesapplication.model.auth.AuthResult
import com.abhinav.notesapplication.model.auth.TokenResponse
import com.abhinav.notesapplication.repository.auth.AuthRepository
import com.abhinav.notesapplication.util.Constants.JWT_TOKEN
import com.google.android.gms.auth.api.Auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class MainViewModel(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val scope = viewModelScope

    //Room Operations
//    fun upsertUser(user: User) {
//        viewModelScope.launch(Dispatchers.IO) {
//            notesDao.upsertUser(user)
//        }
//    }
//
//    fun updateNote(note: Note) {
//        viewModelScope.launch(Dispatchers.IO) {
//            notesDao.updateNote(note)
//        }
//    }
//
//    fun insertNote(note: Note) {
//        viewModelScope.launch(Dispatchers.IO) {
//            notesDao.insertNote(note)
//        }
//    }
//
//    fun deleteNote(id: Int, uId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            notesDao.deleteNote(id, uId)
//        }
//    }
//
//    fun getNoteById(id: Int, uId: String): StateFlow<Note> = notesDao.getNoteById(id, uId)
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Note.emptyNote())
//
//    fun getNotes(uId: String): StateFlow<List<Note>> = notesDao.getNotes(uId)
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _authorizeFeedback = MutableLiveData<AuthResult>()
    val authorizeFeedback : LiveData<AuthResult> get() = _authorizeFeedback

    private val _loginFeedback = MutableLiveData<AuthResult>()
    val loginFeedback : LiveData<AuthResult> get() = _loginFeedback

    private val _signUpFeedback = MutableLiveData<AuthResult>()
    val signUpFeedback : LiveData<AuthResult> get() = _signUpFeedback

    fun signUp(authRequest: AuthRequest) {
        scope.launch(Dispatchers.IO) {
            _loginFeedback.postValue(AuthResult.Loading())
            val result = authRepository.signUp(authRequest)
            handleSignUpResponse(result)
        }
    }

    fun signIn(authRequest: AuthRequest) {
        scope.launch(Dispatchers.IO) {
            _loginFeedback.postValue(AuthResult.Loading())
            val result = authRepository.signIn(authRequest)
            handleSignInResponse(result)
        }
    }

    fun authenticate() {
        val token = sharedPreferences.getString(JWT_TOKEN,"null")
        viewModelScope.launch(Dispatchers.IO) {
            _authorizeFeedback.postValue(AuthResult.Loading())
            val result = authRepository.authenticate(token!!)
            _authorizeFeedback.postValue(
                if (result.isSuccessful) AuthResult.Authorized() else AuthResult.Unauthorized()
            )
        }
    }

    private fun handleSignInResponse(response: Response<TokenResponse>) {
        if (response.isSuccessful) {
            sharedPreferences.edit().putString(JWT_TOKEN,response.body()?.token).apply()
            if (loginFeedback.hasActiveObservers())
                _loginFeedback.postValue(AuthResult.Authorized())
            if (signUpFeedback.hasActiveObservers())
                _signUpFeedback.postValue(AuthResult.Authorized())
        } else {
            _loginFeedback.postValue(AuthResult.UnknownError())
        }
    }

    private fun handleSignUpResponse(response: Response<Unit>) {
        if (response.isSuccessful) {
            _signUpFeedback.postValue(AuthResult.SignedUp())
        } else {
            _signUpFeedback.postValue(AuthResult.UnknownError())
        }
    }

}