package com.example.quizzy.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_user")
data class CachedUser(
        @PrimaryKey
        val id: String,
        val token: String,
        val name: String,
        val email: String
)