package com.abhinav.notesapplication.repository.note

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.abhinav.notesapplication.model.note.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.coroutineContext

class NoteRepositoryImpl(
    private val context: Context,
//    private val coroutineDispatcher: CoroutineDispatcher,
    private val noteLocalDataSource: NoteLocalDataSource,
    private val noteRemoteDataSource: NoteRemoteDataSource
) : NoteRepository {
    override suspend fun insert(note: Note): Boolean {
        return if (isNetworkAvailable(context)) {
            val result = noteRemoteDataSource.insert(note)
            if (result) {
                noteLocalDataSource.insert(note)
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override suspend fun update(note: Note): Boolean {
        return if (isNetworkAvailable(context)) {
            val result = noteRemoteDataSource.update(note)
            if (result) {
                noteLocalDataSource.update(note)
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return if (isNetworkAvailable(context)) {
            val result = noteRemoteDataSource.delete(id)
            if (result) {
                noteLocalDataSource.delete(id)
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun getNotes(): Flow<List<Note>> {
        if (isNetworkAvailable(context)) {
            return flow {
                try {
                    val noteResponse = noteRemoteDataSource.getNotes()
                    if (noteResponse.isSuccess) {
                        val notesList = noteResponse.getOrNull() ?: emptyList()
                        emit(notesList)
                    } else {
                        return@flow
                    }
                } catch (e: Exception) {
                    emit(emptyList())
                }
            }
        } else {
            return noteLocalDataSource.getNotes()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}