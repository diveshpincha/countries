package com.example.myapplication.specific

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.MainActivity.Companion.appId
import com.example.myapplication.MainActivity.Companion.lat
import com.example.myapplication.MainActivity.Companion.lon
import com.example.myapplication.database.CountriesDao
import com.example.myapplication.database.CountriesData
import com.example.myapplication.weatherApi.GetWeather
import com.example.myapplication.weatherApi.ReqData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class SecondViewModel(private val dataSource:CountriesDao,val id : Long):ViewModel() {

    val viewModelJob= Job()
    val coScope= CoroutineScope(Dispatchers.Main+viewModelJob)

    private var _DisData=MutableLiveData<CountriesData>(null)
    val disData:LiveData<CountriesData>
    get() = _DisData


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


    init{
        fetch()
    }

    fun fetch(){
        coScope.launch {
            _DisData.value=dataSource.get(id)
            Log.i("chekc","$lat , $lon ")

            val call = GetWeather.retroGetter.getWeatherByCity(_DisData.value!!.capital, appId)
            try {
                weatherApiData.value=call.await()
                _temperature.value  = ((weatherApiData.value!!.main.temp - 273.15).toInt().toString()+"^C")
                _description.value= weatherApiData.value!!.weather[0].description
                _city.value=weatherApiData.value!!.name
                _imgSrc.value="http://openweathermap.org/img/wn/${weatherApiData.value!!.weather[0].icon}@2x.png"
            } catch (error: Exception){
                Log.i("error",error.toString())
            }
        }
    }




    //val population = disData.value.population.toString()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}