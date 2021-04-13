package com.marou125.covidvaccinationstatus

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.database.CountryRepository
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.lang.reflect.Executable
import java.sql.Date
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CountryListViewModel : ViewModel() {

    val europe = fillEurope()
    val countryRepository: CountryRepository = CountryRepository.get()
    var countryListLiveData = countryRepository.getCountries()
    val executor = Executors.newSingleThreadExecutor()

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



    fun fillEurope(): ArrayList<Country> {
        val europe = ArrayList<Country>()
        europe.add(Country(R.drawable.al, "Albania", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
//        europe.add(Country(R.drawable., "Andorra")) no flag 
        europe.add(Country(R.drawable.at, "Austria", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.by, "Belarus", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.be, "Belgium", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ba, "Bosnia and Herzegovina", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.bg, "Bulgaria", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.hr, "Croatia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.cz, "Czechia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.dk, "Denmark", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ee, "Estonia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.fi, "Finland", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.fr, "France", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ger, "Germany", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.gr, "Greece", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.hu, "Hungary", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ice, "Iceland", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ie, "Ireland", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.it, "Italy", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.lv, "Latvia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.li, "Liechtenstein", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.lt, "Lithuania", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.lu, "Luxembourg", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.mt, "Malta", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.md, "Moldova", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.mc, "Monaco", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.me, "Montenegro", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.nl, "Netherlands", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.mk, "North Macedonia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.no, "Norway", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.pl, "Poland", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.pt, "Portugal", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ro, "Romania", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ru, "Russia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.sm, "San Marino", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.rs, "Serbia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.sk, "Slovakia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.si, "Slovenia", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.es, "Spain", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.se, "Sweden", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ch, "Switzerland", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.ua, "Ukraine", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        europe.add(Country(R.drawable.gb, "United Kingdom", 0, 0, 0, 0, 0, 0, Date(2020,1,1).toString(), 0, 0, 0))
        return europe
    }
}