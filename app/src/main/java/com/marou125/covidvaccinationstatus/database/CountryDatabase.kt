package com.marou125.covidvaccinationstatus.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (entities = [Country::class], version = 2)
@TypeConverters(CountryTypeConverter::class)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
}