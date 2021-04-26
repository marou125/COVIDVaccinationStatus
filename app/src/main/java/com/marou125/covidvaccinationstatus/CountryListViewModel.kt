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
    var americas = fillAmericas()
    var asia = fillAsia()
    var africa = fillAfrica()
    var oceania = fillOceania()
    val countryRepository: CountryRepository = CountryRepository.get()
    var countryListLiveData = countryRepository.getCountries()
    val executor = Executors.newSingleThreadExecutor()

    //boolean for the sort button
    var sortedByName = true


    init {
        insert(europe)
        insert(americas)
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
            europe = europe.sortedBy { it.population }.reversed()
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

    fun fillAmericas(): List<Country>{
        val americas = ArrayList<Country>()
        americas.add(Country(R.drawable.ai,"Anguilla",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ag,"Antigua and Barbuda",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ar,"Argentina",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.aw,"Aruba",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.bs,"Bahamas",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.bb,"Barbados",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.bz,"Belize",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.bm,"Bermuda",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.bo,"Bolivia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.br,"Brazil",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.vg,"British Virgin Islands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ca,"Canada",0,0,0,0,0,0,"2020-01-01",0,0,0))
        //americas.add(Country(R.drawable.,"Caribbean Netherlands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ky,"Cayman Islands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.cl,"Chile",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.co,"Colombia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.cr,"Costa Rica",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.cu,"Cuba",0,0,0,0,0,0,"2020-01-01",0,0,0))
        //americas.add(Country(R.drawable.,"Curacao",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.dm,"Dominica",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.dominican,"Dominican Republic",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ec,"Ecuador",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.sv,"El Salvador",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.fk,"Falkland Islands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.gf,"French Guiana",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.gl,"Greenland",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.gd,"Grenada",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.gp,"Guadeloupe",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.gy,"Guyana",0,0,0,0,0,0,"2020-01-01",0,0,0))
        //americas.add(Country(R.drawable.,"Haiti",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.hn,"Honduras",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.jm,"Jamaica",0,0,0,0,0,0,"2020-01-01",0,0,0))
        //check accuracy of flag
        americas.add(Country(R.drawable.mq,"Martinique",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.mx,"Mexico",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ms,"Montserrat",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ni,"Nicaragua",0,0,0,0,0,0,"2020-01-01",0,0,0))
        //check accuracy of flag
        americas.add(Country(R.drawable.pa,"Panama",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.py,"Paraguay",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.pe,"Peru",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.pr,"Puerto Rico",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.kn,"Saint Kitt & Nevis",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.lc,"Saint Lucia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        //americas.add(Country(R.drawable.,"Saint Martin",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.pm,"Saint Pierre & Miquelon",0,0,0,0,0,0,"2020-01-01",0,0,0))
        //americas.add(Country(R.drawable.,"Sint Marteen",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.vc,"St. Vincent & Grenadines",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.sr,"Suriname",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.tt,"Trinidad & Tobago",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.tc,"Turks and Caicos",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.us,"United States",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.vi,"US Virgin Islands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.uy,"Uruguay",0,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ve,"Venezuela",0,0,0,0,0,0,"2020-01-01",0,0,0))

        return americas
    }

    fun fillAsia(): List<Country>{
        val asia = ArrayList<Country>()
        return asia
    }

    fun fillAfrica(): List<Country>{
        val africa = ArrayList<Country>()
        return africa
    }

    fun fillOceania(): List<Country>{
        val oceania = ArrayList<Country>()
        return oceania
    }
}