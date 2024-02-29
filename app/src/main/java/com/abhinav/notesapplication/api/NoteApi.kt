package com.abhinav.notesapplication.api

import com.abhinav.notesapplication.model.auth.AuthRequest
import com.abhinav.notesapplication.model.auth.TokenResponse
import com.abhinav.notesapplication.model.note.Note
import okhttp3.internal.http2.Http2Reader
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.concurrent.Flow

interface NoteApi {
    @POST("signup")
    suspend fun signUp(
        @Body authRequest: AuthRequest
    ) : Response<Unit>

    @POST("signin")
    suspend fun signIn(
        @Body authRequest: AuthRequest
    ) : Response<TokenResponse>

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    ) : Response<Unit>

    @POST("insertnote")
    suspend fun insert(
        @Body note: Note
    ) : Response<Boolean>

    @PUT("updatenote")
    suspend fun update(
        @Body note: Note
    ) : Response<Boolean>

    @DELETE("deletenote")
    suspend fun delete(
        @Body id: Int
    ) : Response<Boolean>

    @GET("notes")
    fun getNotes() : Response<List<Note>>
}