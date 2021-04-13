package com.marou125.covidvaccinationstatus.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException

private const val DATABASE_NAME = "country-database"

class CountryRepository private constructor(context: Context){

    private val database: CountryDatabase = Room.databaseBuilder(
        context.applicationContext,
        CountryDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val countryDao = database.countryDao()

    fun getCountries(): LiveData<List<Country>> = countryDao.getCountries()

    fun getCountry(name: String): LiveData<Country?> = countryDao.getCountry(name)

    fun insertCountry(country: Country) = countryDao.insertCountry(country)

    fun updateCountry(country: Country) = countryDao.updateCountry(country)

    companion object{
        private var INSTANCE: CountryRepository? = null

        fun initialize(context: Context){
            if(INSTANCE == null) {
                INSTANCE = CountryRepository(context)
            }
        }

        fun get(): CountryRepository{
            return INSTANCE ?:
            throw IllegalStateException("CountryRepository must be initialized")
        }
    }
}