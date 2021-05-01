package com.example.myapplication.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountriesData::class],version = 2,exportSchema = false)
abstract class CountriesDataBase: RoomDatabase() {
    abstract val countriesDao:CountriesDao

    companion object{
        @Volatile
        private var INSTANCE :CountriesDataBase?=null

        fun getInstance(context: Context):CountriesDataBase{
            synchronized(this){
                var instance = INSTANCE

                if(instance==null){
                    Log.i("timepass","db was empty")
                    instance=
                        Room.databaseBuilder(context.applicationContext,CountriesDataBase::class.java,"countries_database")
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE=instance
                }


                return instance
            }
        }
    }
}