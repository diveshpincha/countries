package com.example.myapplication.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.CountriesDao

class MainViewModelFactory(private val dataSource: CountriesDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(dataSource) as T
        }
        throw IllegalArgumentException("gibberish rubbish")
    }
}