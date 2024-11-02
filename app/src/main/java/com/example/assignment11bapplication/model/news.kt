package com.example.assignment11bapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class news(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
):Parcelable