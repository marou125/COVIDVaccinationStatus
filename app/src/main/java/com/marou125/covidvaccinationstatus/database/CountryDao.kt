package com.marou125.covidvaccinationstatus.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CountryDao {

    @Query("SELECT * FROM country")
    fun getCountries(): LiveData<List<Country>>

    @Query("SELECT * FROM country WHERE name=(:name)")
    fun getCountry(name: String): LiveData<Country?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(country: Country)

    @Update
    fun updateCountry(country: Country)

}