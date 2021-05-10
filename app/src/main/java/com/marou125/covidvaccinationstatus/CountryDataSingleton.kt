package com.marou125.covidvaccinationstatus

import android.util.Log
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.service.JsonCaseData
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object CountryDataSingleton {

    var countryDataList = emptyList<VaccinationData>()
    var caseData:JsonCaseData? = null
    var historicCases:JsonCaseData? = null
    var historicDeaths:JsonCaseData? = null

    var europe = fillEurope()
    var americas = fillAmericas()
    var africa = fillAfrica()
    var asiaPacific = createAsiaPacific()
    val world = getAllCountries()

    var favourites = ArrayList<Country>()

    fun fillList(countryData: Array<VaccinationData>){
        countryDataList = countryData.toList()
    }

    fun fillCaseData(jsonData: JsonCaseData){
        caseData = jsonData
    }

    fun fillHistoricConfirmed(jsonData: JsonCaseData){
        historicCases = jsonData
    }

    fun fillHistoricDeaths(jsonData: JsonCaseData){
        historicDeaths = jsonData
    }

    fun saveInFavourites(country: Country) {
        if(!favourites.contains(country)){
            favourites.add(country)
            Log.i("ADDED", "Added $country to favourites")
        } else {
            favourites.remove(country)
            Log.i("REMOVED", "Removed $country from favourites")
        }
    }

    //This function returns the cases/deaths of the last recorded seven days
    fun getWeeklyData(dateMap: HashMap<String, Int>): IntArray {
        val weeklyData = dateMap.entries.sortedByDescending { it.value }.subList(0,7)
        var weeklyDataNumeric = IntArray(7)
        var i = 0
        for(day in weeklyData){
            val numericValue = Integer.valueOf(day.toString().substring(11))
            weeklyDataNumeric[i] = numericValue
            i++
        }
        return weeklyDataNumeric
    }


    fun fillFavourites(savedFavourites: ArrayList<Country>){
        favourites = savedFavourites
    }
    //Population source: https://www.worldometers.info/world-population/population-by-country/
    //TODO: check with right click refactor unused ressources for not used flags for countries that were commented out
    fun fillEurope(): List<Country> {
        val europe = ArrayList<Country>()
        europe.add(Country(R.drawable.al, "Albania", 2877797))
//        europe.add(Country(R.drawable., "Andorra")) no flag
        europe.add(Country(R.drawable.at, "Austria", 9006398))
        europe.add(Country(R.drawable.by, "Belarus", 9449323))
        europe.add(Country(R.drawable.be, "Belgium", 11589623))
        europe.add(Country(R.drawable.ba, "Bosnia and Herzegovina", 3280819))
        europe.add(Country(R.drawable.bg, "Bulgaria", 6948445))
        europe.add(Country(R.drawable.hr, "Croatia", 4105267))
        europe.add(Country(R.drawable.cz, "Czechia", 10708981))
        europe.add(Country(R.drawable.dk, "Denmark", 5792202))
        europe.add(Country(R.drawable.ee, "Estonia", 1326535))
        europe.add(Country(R.drawable.fi, "Finland", 5450720))
        europe.add(Country(R.drawable.fr, "France", 65273511))
        europe.add(Country(R.drawable.ger, "Germany", 83783942))
        europe.add(Country(R.drawable.gr, "Greece", 10423054))
        europe.add(Country(R.drawable.hu, "Hungary", 9660351))
        europe.add(Country(R.drawable.ice, "Iceland", 341243))
        europe.add(Country(R.drawable.ie, "Ireland", 4937786))
        europe.add(Country(R.drawable.it, "Italy", 60461826))
        europe.add(Country(R.drawable.lv, "Latvia", 1886198))
        europe.add(Country(R.drawable.li, "Liechtenstein", 38128))
        europe.add(Country(R.drawable.lt, "Lithuania", 2722289))
        europe.add(Country(R.drawable.lu, "Luxembourg", 625978))
        europe.add(Country(R.drawable.mt, "Malta", 441543))
        europe.add(Country(R.drawable.md, "Moldova", 4033963))
        europe.add(Country(R.drawable.mc, "Monaco", 39242))
        europe.add(Country(R.drawable.me, "Montenegro", 628066))
        europe.add(Country(R.drawable.nl, "Netherlands", 17134872))
        europe.add(Country(R.drawable.mk, "North Macedonia", 2083374))
        europe.add(Country(R.drawable.no, "Norway", 5421241))
        europe.add(Country(R.drawable.pl, "Poland", 37846611))
        europe.add(Country(R.drawable.pt, "Portugal", 10196709))
        europe.add(Country(R.drawable.ro, "Romania", 19237691))
        europe.add(Country(R.drawable.ru, "Russia", 145934462))
        europe.add(Country(R.drawable.sm, "San Marino", 33931))
        europe.add(Country(R.drawable.rs, "Serbia", 8737371))
        europe.add(Country(R.drawable.sk, "Slovakia", 5459642))
        europe.add(Country(R.drawable.si, "Slovenia", 2078938))
        europe.add(Country(R.drawable.es, "Spain", 46754778))
        europe.add(Country(R.drawable.se, "Sweden", 10099265))
        europe.add(Country(R.drawable.ch, "Switzerland", 8654622))
        europe.add(Country(R.drawable.ua, "Ukraine", 43733762))
        europe.add(Country(R.drawable.gb, "United Kingdom", 67886011))



        return europe
    }

    fun fillAmericas(): List<Country>{
        val americas = ArrayList<Country>()
        americas.add(Country(R.drawable.ai,"Anguilla",15003))
        americas.add(Country(R.drawable.ag,"Antigua and Barbuda",97929))
        americas.add(Country(R.drawable.ar,"Argentina",45195774))
        americas.add(Country(R.drawable.aw,"Aruba",106766))
        americas.add(Country(R.drawable.bs,"Bahamas",393244))
        americas.add(Country(R.drawable.bb,"Barbados",287375))
        americas.add(Country(R.drawable.bz,"Belize",397375))
        americas.add(Country(R.drawable.bm,"Bermuda",62278))
        americas.add(Country(R.drawable.bo,"Bolivia",11673021))
        americas.add(Country(R.drawable.br,"Brazil",212559417))
        americas.add(Country(R.drawable.vg,"British Virgin Islands",30231))
        americas.add(Country(R.drawable.ca,"Canada",37742154))
        //americas.add(Country(R.drawable.,"Caribbean Netherlands",26223,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ky,"Cayman Islands",65722))
        americas.add(Country(R.drawable.cl,"Chile",19116201))
        americas.add(Country(R.drawable.co,"Colombia",50882891))
        americas.add(Country(R.drawable.cr,"Costa Rica",5094118))
        americas.add(Country(R.drawable.cu,"Cuba",11326616))
        //americas.add(Country(R.drawable.,"Curacao",164093,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.dm,"Dominica",71986))
        americas.add(Country(R.drawable.dominican,"Dominican Republic",10847910))
        americas.add(Country(R.drawable.ec,"Ecuador",17643054))
        americas.add(Country(R.drawable.sv,"El Salvador",6486205))
        americas.add(Country(R.drawable.fk,"Falkland Islands",3480))
        americas.add(Country(R.drawable.gf,"French Guiana",298682))
        americas.add(Country(R.drawable.gl,"Greenland",56770))
        americas.add(Country(R.drawable.gd,"Grenada",112523))
        americas.add(Country(R.drawable.gp,"Guadeloupe",400124))
        americas.add(Country(R.drawable.gy,"Guyana",786552))
        americas.add(Country(R.drawable.ht,"Haiti",11402528))
        americas.add(Country(R.drawable.hn,"Honduras",9904607))
        americas.add(Country(R.drawable.jm,"Jamaica",2961167))
        //check accuracy of flag
        americas.add(Country(R.drawable.mq,"Martinique",375265))
        americas.add(Country(R.drawable.mx,"Mexico",128932753))
        americas.add(Country(R.drawable.ms,"Montserrat",4992))
        americas.add(Country(R.drawable.ni,"Nicaragua",6624554))
        //check accuracy of flag
        americas.add(Country(R.drawable.pa,"Panama",4314767))
        americas.add(Country(R.drawable.py,"Paraguay",7132538))
        americas.add(Country(R.drawable.pe,"Peru",32971854))
        americas.add(Country(R.drawable.pr,"Puerto Rico",2860853))
        americas.add(Country(R.drawable.kn,"Saint Kitt & Nevis",53199))
        americas.add(Country(R.drawable.lc,"Saint Lucia",183627))
        //americas.add(Country(R.drawable.,"Saint Martin",38666,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.pm,"Saint Pierre & Miquelon",5794))
        //americas.add(Country(R.drawable.,"Sint Marteen",42876,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.vc,"St. Vincent & Grenadines",110940))
        americas.add(Country(R.drawable.sr,"Suriname",586632))
        americas.add(Country(R.drawable.tt,"Trinidad & Tobago",1399488))
        americas.add(Country(R.drawable.tc,"Turks and Caicos",38717))
        americas.add(Country(R.drawable.us,"United States",331002651))
        americas.add(Country(R.drawable.vi,"US Virgin Islands",104425))
        americas.add(Country(R.drawable.uy,"Uruguay",3473730))
        americas.add(Country(R.drawable.ve,"Venezuela",28435940))

        return americas
    }

    //TODO: Grenzfälle prüfen: Zypern, Türkei und Flaggen ergänzen
    fun fillAsia(): List<Country>{
        val asia = ArrayList<Country>()
        asia.add(Country(R.drawable.af,"Afghanistan",38928346))
        asia.add(Country(R.drawable.am,"Armenia",2963243))
        asia.add(Country(R.drawable.az,"Azerbaijan",10139177))
        asia.add(Country(R.drawable.bh,"Bahrain",1701575))
        asia.add(Country(R.drawable.bd,"Bangladesh",164689383))
        asia.add(Country(R.drawable.bi,"Bhutan",771608))
        asia.add(Country(R.drawable.bn,"Brunei",437479))
        asia.add(Country(R.drawable.kh,"Cambodia",16718965))
        asia.add(Country(R.drawable.cn,"China",1439323776))
        asia.add(Country(R.drawable.cy,"Cyprus",1207359))
        asia.add(Country(R.drawable.ge,"Georgia",3989167))
        asia.add(Country(R.drawable.hk,"Hong Kong",7496981))
        asia.add(Country(R.drawable.india,"India",1380004385))
        asia.add(Country(R.drawable.id,"Indonesia",273523615))
        asia.add(Country(R.drawable.ir,"Iran",83992949))
        asia.add(Country(R.drawable.iq,"Iraq",40222493))
        asia.add(Country(R.drawable.il,"Israel",8655535))
        asia.add(Country(R.drawable.jp,"Japan",126476461))
        asia.add(Country(R.drawable.jo,"Jordan",10203134))
        asia.add(Country(R.drawable.kz,"Kazakhstan",18776707))
        asia.add(Country(R.drawable.kw,"Kuwait",4270571))
        asia.add(Country(R.drawable.kg,"Kyrgyzstan",6524195))
        asia.add(Country(R.drawable.la,"Laos",7275560))
        asia.add(Country(R.drawable.lb,"Lebanon",6825445))
        asia.add(Country(R.drawable.mo,"Macao",649335))
        asia.add(Country(R.drawable.my,"Malaysia",32365999))
        asia.add(Country(R.drawable.mv,"Maldives",540544))
        asia.add(Country(R.drawable.mn,"Mongolia",3278290))
        asia.add(Country(R.drawable.mm,"Myanmar",54409800))
        asia.add(Country(R.drawable.np,"Nepal",29136808))
        asia.add(Country(R.drawable.kp,"North Korea",25778816))
        asia.add(Country(R.drawable.om,"Oman",5106626))
        asia.add(Country(R.drawable.pk,"Pakistan",220892340))
        asia.add(Country(R.drawable.ph,"Philippines",109581078))
        asia.add(Country(R.drawable.qa,"Qatar",2881053))
        asia.add(Country(R.drawable.sa,"Saudi Arabia",34813871))
        asia.add(Country(R.drawable.sg,"Singapore",5850342))
        asia.add(Country(R.drawable.kr,"South Korea",51269185))
        asia.add(Country(R.drawable.lk,"Sri Lanka",21413249))
        asia.add(Country(R.drawable.ps,"State of Palestine",5101414))
        asia.add(Country(R.drawable.sy,"Syria",17500658))
        asia.add(Country(R.drawable.tw,"Taiwan",23816775))
        asia.add(Country(R.drawable.tj,"Tajikistan",9537645))
        asia.add(Country(R.drawable.th,"Thailand",69799978))
        asia.add(Country(R.drawable.tl,"Timor-Leste",1318445))
        asia.add(Country(R.drawable.tr,"Turkey",84339067))
        asia.add(Country(R.drawable.tm,"Turkmenistan",6031200))
        asia.add(Country(R.drawable.ae,"United Arab Emirates",9890402))
        asia.add(Country(R.drawable.uz,"Uzbekistan",33469203))
        asia.add(Country(R.drawable.vn,"Vietnam",97338579))
        asia.add(Country(R.drawable.ye,"Yemen",29825964))


        return asia
    }

    fun fillAfrica(): List<Country>{
        val africa = ArrayList<Country>()
        africa.add(Country(R.drawable.dz,"Algeria",43851044))
        africa.add(Country(R.drawable.ao,"Angola",32866272))
        africa.add(Country(R.drawable.bj,"Benin",12123200))
        africa.add(Country(R.drawable.bw,"Botswana",2351627))
        africa.add(Country(R.drawable.bf,"Burkina Faso",20903273))
        africa.add(Country(R.drawable.bi,"Burundi",11890784))
        africa.add(Country(R.drawable.cv,"Cape Verde",555987))
        africa.add(Country(R.drawable.cm,"Cameroon",26545863))
        africa.add(Country(R.drawable.cf,"Central African Republic",4829767))
        africa.add(Country(R.drawable.td,"Chad",16425864))
        africa.add(Country(R.drawable.km,"Comoros",869601))
        africa.add(Country(R.drawable.cg,"Congo",5518087))
        africa.add(Country(R.drawable.ci,"CÃ´te d'Ivoire",26378274))
        africa.add(Country(R.drawable.dj,"Djibouti",988000))
        africa.add(Country(R.drawable.cd,"DR Congo",89651403))
        africa.add(Country(R.drawable.eg,"Egypt",102334404))
        africa.add(Country(R.drawable.gq,"Equatorial Guinea",1402985))
        africa.add(Country(R.drawable.er,"Eritrea",3546421))
        africa.add(Country(R.drawable.sz,"Eswatini",1160164))
        africa.add(Country(R.drawable.et,"Ethiopia",114963588))
        africa.add(Country(R.drawable.ga,"Gabon",2225734))
        africa.add(Country(R.drawable.gm,"Gambia",2416668))
        africa.add(Country(R.drawable.gh,"Ghana",31072940))
        africa.add(Country(R.drawable.gn,"Guinea",13132795))
        africa.add(Country(R.drawable.gw,"Guinea-Bissau",1968001))
        africa.add(Country(R.drawable.ke,"Kenya",53771296))
        africa.add(Country(R.drawable.ls,"Lesotho",2142249))
        africa.add(Country(R.drawable.lr,"Liberia",5057681))
        africa.add(Country(R.drawable.ly,"Libya",6871292))
        africa.add(Country(R.drawable.mg,"Madagascar",27691018))
        africa.add(Country(R.drawable.mw,"Malawi",19129952))
        africa.add(Country(R.drawable.ml,"Mali",20250833))
        africa.add(Country(R.drawable.mr,"Mauritania",4649658))
        africa.add(Country(R.drawable.mu,"Mauritius",1271768))
        africa.add(Country(R.drawable.yt,"Mayotte",272815))
        africa.add(Country(R.drawable.ma,"Morocco",36910560))
        africa.add(Country(R.drawable.mz,"Mozambique",31255435))
        africa.add(Country(R.drawable.na,"Namibia",2540905))
        africa.add(Country(R.drawable.ne,"Niger",24206644))
        africa.add(Country(R.drawable.ng,"Nigeria",206139589))
        africa.add(Country(R.drawable.rw,"Rwanda",12952218))
        africa.add(Country(R.drawable.re,"RÃ©union",895312))
        africa.add(Country(R.drawable.sh,"Saint Helena",6077))
        africa.add(Country(R.drawable.st,"Sao Tome & Principe",219159))
        africa.add(Country(R.drawable.sn,"Senegal",16743927))
        africa.add(Country(R.drawable.sc,"Seychelles",98347))
        africa.add(Country(R.drawable.sl,"Sierra Leone",7976983))
        africa.add(Country(R.drawable.so,"Somalia",15893222))
        africa.add(Country(R.drawable.za,"South Africa",59308690))
//no flag
//africa.add(Country(R.drawable.ssd,"South Sudan",11193725,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.sd,"Sudan",43849260))
        africa.add(Country(R.drawable.tz,"Tanzania",59734218))
        africa.add(Country(R.drawable.tg,"Togo",8278724))
        africa.add(Country(R.drawable.tn,"Tunisia",11818619))
        africa.add(Country(R.drawable.ug,"Uganda",45741007))
        africa.add(Country(R.drawable.eh,"Western Sahara",597339))
        africa.add(Country(R.drawable.zm,"Zambia",18383955))
        africa.add(Country(R.drawable.zw,"Zimbabwe",14862924))


        return africa
    }

    fun fillOceania(): List<Country>{
        val oceania = ArrayList<Country>()
        //check flag
        oceania.add(Country(R.drawable.americansamoa,"American Samoa",55191))
        oceania.add(Country(R.drawable.au,"Australia",25499884))
        oceania.add(Country(R.drawable.ck,"Cook Islands",17564))
        oceania.add(Country(R.drawable.fj,"Fiji",896445))
        oceania.add(Country(R.drawable.pf,"French Polynesia",280908))
        oceania.add(Country(R.drawable.gu,"Guam",168775))
        oceania.add(Country(R.drawable.ki,"Kiribati",119449))
        oceania.add(Country(R.drawable.mh,"Marshall Islands",59190))
        oceania.add(Country(R.drawable.fm,"Micronesia",548914))
        oceania.add(Country(R.drawable.nr,"Nauru",10824))
        oceania.add(Country(R.drawable.nc,"New Caledonia",2854980))
        oceania.add(Country(R.drawable.nz,"New Zealand",4822233))
        oceania.add(Country(R.drawable.nu,"Niue",1626))
        oceania.add(Country(R.drawable.mp,"Northern Mariana Islands",57559))
        oceania.add(Country(R.drawable.pw,"Palau",18094))
        oceania.add(Country(R.drawable.pg,"Papua New Guinea",8947024))
        oceania.add(Country(R.drawable.ws,"Samoa",198414))
        oceania.add(Country(R.drawable.sb,"Solomon Islands",686884))
        oceania.add(Country(R.drawable.tk,"Tokelau",1357))
        oceania.add(Country(R.drawable.to,"Tonga",105695))
        oceania.add(Country(R.drawable.tv,"Tuvalu",11792))
        oceania.add(Country(R.drawable.vu,"Vanuatu",307145))
        oceania.add(Country(R.drawable.wf,"Wallis & Futuna",11239))

        return oceania
    }

    fun createAsiaPacific(): List<Country>{
        var asiaOceania = ArrayList<Country>()
        asiaOceania.addAll(fillAsia())
        asiaOceania.addAll(fillOceania())
        return asiaOceania.sortedBy { it.name }
    }

    fun getAllCountries(): List<Country>{
        var world = ArrayList<Country>()
        world.addAll(europe)
        world.addAll(africa)
        world.addAll(americas)
        world.addAll(asiaPacific)
        return world.sortedBy { it.name }
    }


}