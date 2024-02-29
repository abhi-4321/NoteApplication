package com.abhinav.notesapplication.repository.auth

import com.abhinav.notesapplication.model.auth.AuthRequest
import com.abhinav.notesapplication.model.auth.AuthResult
import com.abhinav.notesapplication.model.auth.TokenResponse
import retrofit2.Call
import retrofit2.Response

interface AuthRepository {
    suspend fun signUp(authRequest: AuthRequest) : Response<Unit>
    suspend fun signIn(authRequest: AuthRequest) : Response<TokenResponse>
    suspend fun authenticate(token: String) : Response<Unit>
}