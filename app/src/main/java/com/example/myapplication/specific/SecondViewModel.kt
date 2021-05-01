package com.example.myapplication.specific

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.database.CountriesDao
import com.example.myapplication.database.CountriesData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SecondViewModel(private val dataSource:CountriesDao,val id : Long):ViewModel() {

    val viewModelJob= Job()
    val coScope= CoroutineScope(Dispatchers.Main+viewModelJob)

    private var _DisData=MutableLiveData<CountriesData>()
    val disData:LiveData<CountriesData>
    get() = _DisData

    init{
        fetch()
    }

    fun fetch(){
        coScope.launch {
            _DisData.value=dataSource.get(id)
        }
    }

    //val population = disData.value.population.toString()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}