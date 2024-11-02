package com.example.assignment11bapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataBaseNews (
    @PrimaryKey
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: String

)