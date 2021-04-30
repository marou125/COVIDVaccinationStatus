package com.marou125.covidvaccinationstatus.database


import androidx.room.*
import java.util.*

@Entity
data class Country(val flag: Int,
                   @PrimaryKey val name: String,
                   var population: Int,
                   var totalCases: Int = 0,
                   var newCases: Int = 0,
                   var activeCases: Int = 0,
                   var totalDeaths: Int = 0,
                   var newDeaths: Int = 0,
                   var date: String = "2021-01-01",
                   var totalVaccinations: Int = 0,
                   var firstVaccine: Int = 0,
                   var fullyVaccinated: Int = 0,
                   var sevenDayAverage: Int = 0 ) {
    @Override
    override fun toString(): String {
        return name
    }
}