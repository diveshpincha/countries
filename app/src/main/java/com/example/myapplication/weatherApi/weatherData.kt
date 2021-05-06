package com.example.myapplication.weatherApi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class ReqData(
        var weather:Array<weatherData>,
        var main:Main,
        var name:String
)

data class Main(
        var temp:Double
)

data class weatherData(
        var description:String,
        var icon:String
)
