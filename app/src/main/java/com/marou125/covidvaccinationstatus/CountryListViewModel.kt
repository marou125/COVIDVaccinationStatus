package com.marou125.covidvaccinationstatus

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.database.CountryRepository
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.lang.reflect.Executable
import java.security.AccessController.getContext
import java.sql.Date
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CountryListViewModel : ViewModel() {

    var europe = fillEurope()
    val countryRepository: CountryRepository = CountryRepository.get()
    var countryListLiveData = countryRepository.getCountries()
    val executor = Executors.newSingleThreadExecutor()

    //boolean for the sort button
    var sortedByName = true


    init {
        insert(europe)
    }


    //fills database with countries if freshly installed
    private fun insert(europe: List<Country>) {
        if(countryListLiveData.value == null){
            executor.execute{
                for (country in europe){
                    countryRepository.insertCountry(country)
                }
            }
            countryListLiveData = countryRepository.getCountries()
        }
    }

    fun sortCountries(){
        if(sortedByName) {
            europe = europe.sortedBy { it.population }
            sortedByName = false
        } else {
            europe = europe.sortedBy{it.name}
            sortedByName = true
        }

    }


//Population source: https://www.worldometers.info/world-population/population-by-country/
    fun fillEurope(): List<Country> {
        val europe = ArrayList<Country>()
        europe.add(Country(R.drawable.al, "Albania", 2877797, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
//        europe.add(Country(R.drawable., "Andorra")) no flag 
        europe.add(Country(R.drawable.at, "Austria", 9006398, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.by, "Belarus", 9449323, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.be, "Belgium", 11589623, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ba, "Bosnia and Herzegovina", 3280819, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.bg, "Bulgaria", 6948445, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.hr, "Croatia", 4105267, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.cz, "Czechia", 10708981,0, 0, 0, 0, 0,  Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.dk, "Denmark", 5792202, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ee, "Estonia", 1326535, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.fi, "Finland", 5450720, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.fr, "France", 65273511, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ger, "Germany", 83783942, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.gr, "Greece", 10423054, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.hu, "Hungary", 9660351, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ice, "Iceland", 341243, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ie, "Ireland", 4937786, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.it, "Italy", 60461826, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.lv, "Latvia", 1886198, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.li, "Liechtenstein", 38128, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.lt, "Lithuania", 2722289, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.lu, "Luxembourg", 625978, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.mt, "Malta", 441543, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.md, "Moldova", 4033963, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.mc, "Monaco", 39242, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.me, "Montenegro", 628066, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.nl, "Netherlands", 17134872, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.mk, "North Macedonia", 2083374, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.no, "Norway", 5421241, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.pl, "Poland", 37846611, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.pt, "Portugal", 10196709, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ro, "Romania", 19237691, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ru, "Russia", 145934462, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.sm, "San Marino", 33931, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.rs, "Serbia", 8737371, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.sk, "Slovakia", 5459642, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.si, "Slovenia", 2078938, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.es, "Spain", 46754778, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.se, "Sweden", 10099265, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ch, "Switzerland", 8654622, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ua, "Ukraine", 43733762, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.gb, "United Kingdom", 67886011, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        return europe
    }
}