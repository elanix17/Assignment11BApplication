package com.example.assignment11bapplication.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment11application.retrofit.RetrofitInstance
import com.example.assignment11bapplication.model.Article
import com.example.assignment11bapplication.repository.NewsRepository
import kotlinx.coroutines.launch


enum class ApiStatus { LOADING, ERROR, DONE }

class ListViewModel(private val repository: NewsRepository):ViewModel() {
    private val _properties = MutableLiveData<List<Article>>()
    val properties: LiveData<List<Article>>
        get() = _properties

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    init {
        getProperties()
    }

    private fun getProperties() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            val cachedNews = repository.getCachedNews()
            if (!cachedNews.isNullOrEmpty()) {
                _properties.value = cachedNews.map { cachedArticle ->
                    Article(
                        author = "", // Add any missing fields if necessary
                        content = cachedArticle.description,  // Use description for content
                        description = cachedArticle.description,
                        publishedAt = cachedArticle.publishedAt,
                        source = null, // Replace with actual Source if needed
                        title = cachedArticle.title,
                        url = "", // Add URL if needed
                        urlToImage = cachedArticle.urlToImage
                    )
                }
                _status.value = ApiStatus.DONE
            } else {
                // If no cached data, fetch from API
                fetchNewsFromApi()
            }
        }
    }



private fun fetchNewsFromApi() {
    viewModelScope.launch {
        try {
            repository.refreshNews() // Fetch from API and update the database

            // After refreshing, retrieve the cached news again
            val cachedNews = repository.getCachedNews()
            _properties.value = cachedNews.map { cachedArticle ->
                Article(
                    author = "", // Add missing fields if necessary
                    content = cachedArticle.description, // Use description for content
                    description = cachedArticle.description,
                    publishedAt = cachedArticle.publishedAt,
                    source = null, // Replace with actual Source if needed
                    title = cachedArticle.title,
                    url = "", // Add URL if needed
                    urlToImage = cachedArticle.urlToImage
                )
            }
            _status.value = ApiStatus.DONE
        } catch (e: Exception) {
            _status.value = ApiStatus.ERROR
            e.printStackTrace() // Log the exception
        }
    }
}
}

class NewsViewModelFactory(
    private val repository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}