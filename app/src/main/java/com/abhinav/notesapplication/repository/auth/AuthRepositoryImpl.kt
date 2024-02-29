package com.abhinav.notesapplication.repository.auth

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresExtension
import com.abhinav.notesapplication.api.NoteApi
import com.abhinav.notesapplication.model.auth.AuthRequest
import com.abhinav.notesapplication.model.auth.TokenResponse
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: NoteApi,
) : AuthRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun signUp(authRequest: AuthRequest): Response<Unit> {
        return api.signUp(authRequest)
    }

    override suspend fun signIn(authRequest: AuthRequest): Response<TokenResponse> {
        return api.signIn(authRequest)
    }

    override suspend fun authenticate(token: String): Response<Unit> {
        return api.authenticate("Bearer $token")
    }
}