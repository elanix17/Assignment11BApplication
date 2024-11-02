package com.example.assignment11bapplication.retrofit

import com.example.assignment11bapplication.model.Article
import com.example.assignment11bapplication.model.news
import retrofit2.http.GET
import retrofit2.Response

interface NewsAPI {
    @GET("v2/top-headlines?country=us&category=business&apiKey=3c8c6f0bb37d42338590a7cfafa3fe11")
    suspend fun getNews():Response<news>
}