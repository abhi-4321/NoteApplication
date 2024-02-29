package com.abhinav.notesapplication.model.auth

sealed class AuthResult(private val data: TokenResponse = TokenResponse("")){
    class Authorized : AuthResult()
    class Unauthorized : AuthResult()
    class UnknownError : AuthResult()
    class Loading() : AuthResult()
    class SignedUp() : AuthResult()
}