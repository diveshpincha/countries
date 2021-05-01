package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CountriesDao {
    @Insert
    fun insert(country : CountriesData)

    @Query("select * from countries_table")
    suspend fun display(): List<CountriesData>

    @Query("select * from countries_table where id = :key")
    suspend fun get(key:Long):CountriesData
}