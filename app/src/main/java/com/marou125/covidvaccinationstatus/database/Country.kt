package com.marou125.covidvaccinationstatus.database


import androidx.room.*
import java.util.*

@Entity
data class Country(val flag: Int,
                   @PrimaryKey val name: String,
                   var population: Int,
                   var totalCases: Int,
                   var newCases: Int,
                   var activeCases: Int,
                   var totalDeaths: Int,
                   var newDeaths: Int,
                   var date: String,
                   var totalVaccinations: Int,
                   var firstVaccine: Int,
                   var fullyVaccinated: Int) {
    @Override
    override fun toString(): String {
        return name
    }
}