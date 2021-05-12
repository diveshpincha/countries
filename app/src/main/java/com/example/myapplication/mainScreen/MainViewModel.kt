package com.example.myapplication.mainScreen

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.database.CountriesDao
import com.example.myapplication.database.CountriesData
import com.example.myapplication.network.Countries
import com.example.myapplication.network.CountriesApi
import kotlinx.coroutines.*

class MainViewModel ( private val dataSource : CountriesDao): ViewModel() {

    private var _properties = MutableLiveData<List<Countries>>()
    val properties: LiveData<List<Countries>>
        get() = _properties

    private var _dbcountry = MutableLiveData<List<CountriesData>>()
    val dbcountry: LiveData<List<CountriesData>>
        get() = _dbcountry


    val viewModelJob = Job()
    val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getCountries()
    }

    fun insert() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _properties.value?.forEach {
                    val idata = CountriesData()
                    idata.capital = it.capital
                    idata.population = it.population
                    idata.name = it.name
                    idata.region = it.region
                    idata.flag = it.flag
                    dataSource.insert(idata)
                    Log.i("db", "insert")
                }
            }
        }
    }

    private fun getCountries() {

        viewModelScope.launch {
            _dbcountry.value = dataSource.display()


            if (_dbcountry.value?.size == 0) {
                viewModelScope.launch {
                    val getDeferred = CountriesApi.retrofitGetter.getAllCountries()

                    try {
                        _properties.value = getDeferred.await()
                    } catch (error: Exception) {
                        Log.i("error", "erroe")
                    }
                    insert()
                }

            }
        }
    }

    private val _selected = MutableLiveData<CountriesData>()
    val selected: LiveData<CountriesData>
        get() = _selected

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private var _navId = MutableLiveData<Long?>()
    val navId: LiveData<Long?>
        get() = _navId

    fun ForNav(idd: Long?) {
        _navId.value = idd
    }

    fun navDone() {
        _navId.value = null
    }

    fun filter(s: Editable): MutableList<CountriesData> {
        val temp: MutableList<CountriesData> = ArrayList()
        for (d in dbcountry.value!!) {
            //or use .equal(text) with you want equal match
            if (d.name.contains(s, true)) {
                temp.add(d)
            }
        }
        return temp
    }
}