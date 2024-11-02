// MainViewModel.kt
package com.example.assignment11bapplication

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.assignment11bapplication.model.WeatherResponse
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val weatherLiveData = MutableLiveData<WeatherResponse>()
    val weatherIcon = MutableLiveData<Drawable?>()
    val cityName = MutableLiveData<String>()
    val temperature = MutableLiveData<String>()

    private val client = OkHttpClient()

    fun fetchWeatherData(lat: Double, lon: Double) {
        val apiKey = "970ef270d983c1edd4a58935d5e76e4b"
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric"

        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("fetchWeatherData", "Error fetching weather data", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val responseString = responseBody.string()
                        val json = JSONObject(responseString)
                        val iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon")
                        val city = json.getString("name")
                        val temp = json.getJSONObject("main").getDouble("temp").toString() + "°C"

                        cityName.postValue(city)
                        temperature.postValue(temp)
                        updateWeatherIcon(iconCode)
                    }
                } else {
                    Log.e("fetchWeatherData", "Unsuccessful response")
                }
            }
        })
    }

    private fun updateWeatherIcon(iconCode: String) {
        val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
        Glide.with(getApplication<Application>().applicationContext)
            .load(iconUrl)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    weatherIcon.postValue(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}
