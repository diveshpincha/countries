package com.example.myapplication.specific

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.CountriesDao

class SecondViewModelFactory(private val dataSource: CountriesDao,val id : Long):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondViewModel::class.java)){
            return SecondViewModel(dataSource,id) as T
        }
        throw IllegalArgumentException("timepass matt karr")
    }
}