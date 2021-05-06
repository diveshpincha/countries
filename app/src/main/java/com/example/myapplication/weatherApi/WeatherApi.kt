package com.example.myapplication.weatherApi

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val baseUrl2 = "http://api.openweathermap.org/"

private val moshi2 = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi2))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(baseUrl2)
        .build()


interface WeatherApi {
    @GET("data/2.5/weather?")
    fun getWeather(@Query("lat") lat : String , @Query("lon") lon: String, @Query("APPID") app_id: String):Deferred<ReqData>

    @GET("data/2.5/weather?")
    fun getWeatherByCity(@Query("q") city_name : String , @Query("APPID") app_id: String):Deferred<ReqData>
}

object GetWeather{
    val retroGetter:WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}