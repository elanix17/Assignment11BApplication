package com.example.assignment11bapplication.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.assignment11application.retrofit.RetrofitInstance
import com.example.assignment11bapplication.database.DataBaseNews
import com.example.assignment11bapplication.database.NewsDatabase
import com.example.assignment11bapplication.model.news
import com.example.assignment11bapplication.retrofit.NewsAPI
import retrofit2.Response
import java.util.concurrent.TimeUnit

class ApiWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val apiService: NewsAPI = RetrofitInstance.NewsApi

    override suspend fun doWork(): Result {
        return try {
            // Fetch news from API
            val response: Response<news> = apiService.getNews()
            if (response.isSuccessful) {
                // Store the data in the database
                val newsData = response.body()
                if (newsData != null) {
                    val db = NewsDatabase.getDatabase(applicationContext) // Use a singleton instance
                    db.userDao().clearAll()
                    db.userDao().insertAll(newsData.articles.map { article ->
                        DataBaseNews(
                            title = article.title ?: "",
                            description = article.description ?: "",
                            urlToImage = article.urlToImage ?: "",
                            publishedAt = article.publishedAt ?: ""
                        )
                    })
                }
                Result.success()
            } else {
                Result.retry() // Retry if the API call was not successful
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure() // Return failure if there was an exception
        }
    }

    companion object {
        fun createWorkRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<ApiWorker>(1, TimeUnit.HOURS)
                .build()
        }
    }
}
