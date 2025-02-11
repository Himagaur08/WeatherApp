package com.android.myweatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    private const val base_Url = "https://api.weatherapi.com"

     private fun getInstance() : Retrofit{
           return Retrofit.Builder()
               .baseUrl(base_Url).addConverterFactory(GsonConverterFactory.create())
               .build()
    }

    val weatherApi : WeatherApi = getInstance().create(WeatherApi::class.java)
}


