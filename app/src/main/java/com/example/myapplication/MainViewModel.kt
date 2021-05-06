package com.example.myapplication

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.MainActivity.Companion.appId
import com.example.myapplication.MainActivity.Companion.lat
import com.example.myapplication.MainActivity.Companion.lon
import com.example.myapplication.weatherApi.GetWeather
import com.example.myapplication.weatherApi.ReqData
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class Main_ViewModel:ViewModel() {

    private var _temperature=MutableLiveData<String>()

    val temperature :LiveData<String>
        get() = _temperature

    private var _description =MutableLiveData<String>()

    val description :LiveData<String>
        get() = _description

    private var _city=MutableLiveData<String>()
    val city : LiveData<String>
    get() = _city

    private var _imgSrc=MutableLiveData<String>()
    val imgSrc : LiveData<String>
        get() = _imgSrc


    private var weatherApiData = MutableLiveData<ReqData>()


    val ModelJob = Job()

    val coroutineScope = CoroutineScope(ModelJob+Dispatchers.Main)

    //init {
    fun invokee(){
        coroutineScope.launch {
            val call = GetWeather.retroGetter.getWeather(
                lat.value!!,
                lon,
                appId
            )
            try {
                weatherApiData.value = call.await()
                _temperature.value  = ((weatherApiData.value!!.main.temp - 273.15).toInt().toString()+"^C")
                _description.value= weatherApiData.value!!.weather[0].description
                _city.value=weatherApiData.value!!.name
                _imgSrc.value="http://openweathermap.org/img/wn/${weatherApiData.value!!.weather[0].icon}@2x.png"

            } catch (error:Exception){
                Log.i("error",error.toString())
            }
            //Log.i("abc", abc.value!!.toString())
        }
    }


}

