package com.abhinav.notesapplication.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.abhinav.notesapplication.api.NoteApi
import com.abhinav.notesapplication.database.NotesDB
import com.abhinav.notesapplication.repository.auth.AuthRepository
import com.abhinav.notesapplication.repository.auth.AuthRepositoryImpl
import com.abhinav.notesapplication.repository.note.NoteLocalDataSource
import com.abhinav.notesapplication.repository.note.NoteRemoteDataSource
import com.abhinav.notesapplication.repository.note.NoteRepository
import com.abhinav.notesapplication.repository.note.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAuthApi(): NoteApi {
        val logging = HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().also {
            it.addInterceptor(logging)
        }

        return Retrofit.Builder().baseUrl("http://192.168.1.7:8080")
            .client(client.build())
            .addConverterFactory(MoshiConverterFactory.create()).build().create(NoteApi::class.java)
    }

    @Provides
    @Singleton
    fun providesSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("APP", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(api: NoteApi, sharedPreferences: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(api)
    }

//    @Provides
//    @Singleton
//    fun providesNotesDB(@ApplicationContext context: Context): NotesDB {
//        return Room.databaseBuilder(context, NotesDB::class.java, "noteDb").build()
//    }
//
//    @Provides
//    @Singleton
//    fun providesNoteRepository(
////        coroutineDispatcher: CoroutineDispatcher,
//        localDataSource: NoteLocalDataSource,
//        remoteDataSource: NoteRemoteDataSource,
//        context: Context
//    ): NoteRepository {
//        return NoteRepositoryImpl(context,localDataSource,remoteDataSource)
//    }


}