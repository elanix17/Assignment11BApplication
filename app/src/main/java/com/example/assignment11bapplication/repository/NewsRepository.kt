package com.example.assignment11bapplication.repository

import android.util.Log
import com.example.assignment11bapplication.database.DataBaseNews
import com.example.assignment11bapplication.database.NewsDatabase
import com.example.assignment11bapplication.model.news
import com.example.assignment11bapplication.retrofit.NewsAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NewsRepository(private val database: NewsDatabase, private val api: NewsAPI) {

    // Function to refresh data by fetching from the API and storing in the database
    suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            try {
                // Fetch news from API
                Log.d("NewsRepository", "Starting API call to fetch news")
                val response: Response<news> = api.getNews()
                if (response.isSuccessful) {
                    Log.d("NewsRepository", "API call successful, processing response")
                    response.body()?.let { newsResponse ->
                        // Map the articles from the API response to database entity
                        val databaseNews = newsResponse.articles.map { article ->
                            DataBaseNews(
                                title = article.title ?: "",
                                description = article.description ?: "",
                                urlToImage = article.urlToImage ?: "",
                                publishedAt = article.publishedAt ?: ""
                            )
                        }
                        // Save the mapped data into the database
                        database.userDao().insertAll(databaseNews)
                        Log.d("NewsRepository", "Data inserted into database: ${databaseNews.size} items")

                    }
                } else {
                    Log.e("NewsRepository", "API Call Failed: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getCachedNews(): List<DataBaseNews> {
        return database.userDao().getNewsList()
    }
}
