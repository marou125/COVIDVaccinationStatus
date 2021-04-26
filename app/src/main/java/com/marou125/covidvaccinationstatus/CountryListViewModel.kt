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

    //TODO: Grenzfälle prüfen: Zypern, Türkei und Flaggen ergänzen
    fun fillAsia(): List<Country>{
        val asia = ArrayList<Country>()
        asia.add(Country(R.drawable.af,"Afghanistan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.am,"Armenia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.az,"Azerbaijan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.bh,"Bahrain",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.bd,"Bangladesh",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.bi,"Bhutan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.bn,"Brunei",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.kh,"Cambodia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.cn,"China",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.cy,"Cyprus",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.ge,"Georgia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.hk,"Hong Kong",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.india,"India",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.id,"Indonesia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.ir,"Iran",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.iq,"Iraq",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.il,"Israel",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.jp,"Japan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.jo,"Jordan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.kz,"Kazakhstan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.kw,"Kuwait",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.kg,"Kyrgyzstan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.la,"Laos",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.lb,"Lebanon",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.mo,"Macao",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.my,"Malaysia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.mv,"Maldives",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.mn,"Mongolia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.mm,"Myanmar",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.np,"Nepal",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.kp,"North Korea",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.om,"Oman",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.pk,"Pakistan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.ph,"Philippines",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.qa,"Qatar",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.sa,"Saudi Arabia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.sg,"Singapore",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.kr,"South Korea",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.lk,"Sri Lanka",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.ps,"State of Palestine",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.sy,"Syria",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.tw,"Taiwan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.tj,"Tajikistan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.th,"Thailand",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.tl,"Timor-Leste",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.tr,"Turkey",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.tm,"Turkmenistan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.ae,"United Arab Emirates",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.uz,"Uzbekistan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.vn,"Vietnam",0,0,0,0,0,0,"2020-01-01",0,0,0))
        asia.add(Country(R.drawable.ye,"Yemen",0,0,0,0,0,0,"2020-01-01",0,0,0))

        return asia
    }

    fun fillAfrica(): List<Country>{
        val africa = ArrayList<Country>()
        africa.add(Country(R.drawable.dz,"Algeria",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ao,"Angola",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.bj,"Benin",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.bw,"Botswana",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.bf,"Burkina Faso",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.bi,"Burundi",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.cv,"Cape Verde",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.cm,"Cameroon",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.cf,"Central African Republic",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.td,"Chad",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.km,"Comoros",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.cg,"Congo",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ci,"Côte d'Ivoire",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.dj,"Djibouti",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.cd,"DR Congo",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.eg,"Egypt",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.gq,"Equatorial Guinea",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.er,"Eritrea",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.sz,"Eswatini",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.et,"Ethiopia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ga,"Gabon",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.gm,"Gambia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.gh,"Ghana",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.gn,"Guinea",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.gw,"Guinea-Bissau",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ke,"Kenya",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ls,"Lesotho",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.lr,"Liberia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ly,"Libya",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.mg,"Madagascar",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.mw,"Malawi",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ml,"Mali",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.mr,"Mauritania",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.mu,"Mauritius",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.yt,"Mayotte",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ma,"Morocco",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.mz,"Mozambique",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.na,"Namibia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ne,"Niger",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ng,"Nigeria",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.rw,"Rwanda",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.re,"Réunion",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.sh,"Saint Helena",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.st,"Sao Tome & Principe",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.sn,"Senegal",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.sc,"Seychelles",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.sl,"Sierra Leone",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.so,"Somalia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.za,"South Africa",0,0,0,0,0,0,"2020-01-01",0,0,0))
        //no flag
        //africa.add(Country(R.drawable.ssd,"South Sudan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.sd,"Sudan",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.tz,"Tanzania",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.tg,"Togo",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.tn,"Tunisia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.ug,"Uganda",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.eh,"Western Sahara",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.zm,"Zambia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.zw,"Zimbabwe",0,0,0,0,0,0,"2020-01-01",0,0,0))

        return africa
    }

    fun fillOceania(): List<Country>{
        val oceania = ArrayList<Country>()
        //check flag
        oceania.add(Country(R.drawable.americansamoa,"American Samoa",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.au,"Australia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.ck,"Cook Islands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.fj,"Fiji",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.pf,"French Polynesia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.gu,"Guam",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.ki,"Kiribati",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.mh,"Marshall Islands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.fm,"Micronesia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.nr,"Nauru",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.nc,"New Caledonia",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.nz,"New Zealand",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.nu,"Niue",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.mp,"Northern Mariana Islands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.pw,"Palau",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.pg,"Papua New Guinea",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.ws,"Samoa",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.sb,"Solomon Islands",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.tk,"Tokelau",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.to,"Tonga",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.tv,"Tuvalu",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.vu,"Vanuatu",0,0,0,0,0,0,"2020-01-01",0,0,0))
        oceania.add(Country(R.drawable.wf,"Wallis & Futuna",0,0,0,0,0,0,"2020-01-01",0,0,0))
        return oceania
    }
}