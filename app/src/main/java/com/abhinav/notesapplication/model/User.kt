package com.abhinav.notesapplication.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val uId: Int,
    val username: String
)
