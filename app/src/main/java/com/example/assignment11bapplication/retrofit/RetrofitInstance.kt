package com.example.assignment11application.retrofit

import android.util.Log
import com.example.assignment11bapplication.retrofit.NewsAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private  val BASE_URL ="https://newsapi.org/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val NewsApi: NewsAPI by lazy {
        retrofit.create(NewsAPI::class.java)
    }
}