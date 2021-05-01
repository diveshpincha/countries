package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries_table")
data class CountriesData(
    @PrimaryKey(autoGenerate = true)
    var id:Long=0L,
     @ColumnInfo(name="Flag")
    var flag:String="",
     @ColumnInfo(name="Country")
    var name : String="",
     @ColumnInfo(name="capital")
    var capital : String="",
     @ColumnInfo(name="continent")
    var region: String="",
 //   @ColumnInfo(name="calling_code")
   // val callingCodes : String,
     @ColumnInfo(name="population")
    var population : Long=0L

)
