package com.marou125.covidvaccinationstatus

import android.content.Context
import android.util.Log
import com.marou125.covidvaccinationstatus.Utility.App
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.service.JsonCaseData
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.lang.StringBuilder
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

    fun findCountryByName(context: Context, country: String): Country{
        for(c in world){
            if(context.resources.getString(c.name) == country){
                return c
            }
        }
        return world[1]
    }

    //TODO: Algorithmus schreiben um diese Funktion zu vervollständigen
    fun getEnglishCountryName(countryID: Int): String{
        var english = ""
        when(countryID){
            R.string.Afghanistan -> english = "Afghanistan"
            R.string.Albania -> english = "Albania"
            R.string.Algeria -> english = "Algeria"
            R.string.AmericanSamoa -> english = "American Samoa"
            //R.string.Andorra -> english = "Andorra"
            R.string.Angola -> english = "Angola"
            R.string.Anguilla -> english = "Anguilla"
            R.string.Antarctica -> english = "Antarctica"
            R.string.AntiguaandBarbuda -> english = "Antigua and Barbuda"
            R.string.Argentina -> english = "Argentina"
            R.string.Armenia -> english = "Armenia"
            R.string.Aruba -> english = "Aruba"
            R.string.Australia -> english = "Australia"
            R.string.Austria -> english = "Austria"
            R.string.Azerbaijan -> english = "Azerbaijan"
            R.string.Bahamas -> english = "Bahamas"
            R.string.Bahrain -> english = "Bahrain"
            R.string.Bangladesh -> english = "Bangladesh"
            R.string.Barbados -> english = "Barbados"
            R.string.Belarus -> english = "Belarus"
            R.string.Belgium -> english = "Belgium"
            R.string.Belize -> english = "Belize"
            R.string.Benin -> english = "Benin"
            R.string.Bermuda -> english = "Bermuda"
            R.string.Bhutan -> english = "Bhutan"
            R.string.Bolivia -> english = "Bolivia"
            R.string.BosniaandHerzegovina -> english = "Bosnia and Herzegovina"
            R.string.Botswana -> english = "Botswana"
            R.string.BouvetIsland -> english = "Bouvet Island"
            R.string.Brazil -> english = "Brazil"
            //R.string.BritishIndianOceanTerritory -> english = "British Indian Ocean Territory"
            R.string.BruneiDarussalam -> english = "Brunei"
            R.string.Bulgaria -> english = "Bulgaria"
            R.string.BurkinaFaso -> english = "Burkina Faso"
            R.string.Burundi -> english = "Burundi"
            R.string.Cambodia -> english = "Cambodia"
            R.string.Cameroon -> english = "Cameroon"
            R.string.Canada -> english = "Canada"
            R.string.CapeVerde -> english = "Cape Verde"
            R.string.CaymanIslands -> english = "Cayman Islands"
            R.string.CentralAfricanRepublic -> english = "Central African Republic"
            R.string.Chad -> english = "Chad"
            R.string.Chile -> english = "Chile"
            R.string.China -> english = "China"
            R.string.ChristmasIsland -> english = "Christmas Island"
            R.string.CocosIslands -> english = "Cocos Islands"
            R.string.Colombia -> english = "Colombia"
            R.string.Comoros -> english = "Comoros"
            R.string.Congo -> english = "Congo"
            R.string.DRCongoe -> english = "Democratic Republic of Congo"
            R.string.CookIslands -> english = "Cook Islands"
            R.string.CostaRica -> english = "Costa Rica"
            R.string.CoteDIvoire -> english = "Cote d'Ivoire"
            R.string.Croatia -> english = "Croatia"
            R.string.Cuba -> english = "Cuba"
            R.string.Cyprus -> english = "Cyprus"
            R.string.CzechRepublic -> english = "Czechia"
            R.string.Denmark -> english = "Denmark"
            R.string.Djibouti -> english = "Djibouti"
            R.string.Dominica -> english = "Dominica"
            R.string.DominicanRepublic -> english = "Dominican Republic"
            R.string.EastTimor -> english = "Timor"
            R.string.Ecuador -> english = "Ecuador"
            R.string.Egypt -> english = "Egypt"
            R.string.ElSalvador -> english = "El Salvador"
            R.string.EquatorialGuinea -> english = "Equatorial Guinea"
            R.string.Eritrea -> english = "Eritrea"
            R.string.Estonia -> english = "Estonia"
            R.string.Eswatini -> english = "Eswatini"
            R.string.Ethiopia -> english = "Ethiopia"
            R.string.FalklandIslands -> english = "Falkland Islands"
            R.string.FaroeIslands -> english = "Faroe Islands"
            R.string.Fiji -> english = "Fiji"
            R.string.Finland -> english = "Finland"
            R.string.France -> english = "France"
            //R.string.FranceMetropolitan -> english = "France Metropolitan"
            R.string.FrenchGuiana -> english = "French Guiana"
            R.string.FrenchPolynesia -> english = "French Polynesia"
            //R.string.FrenchSouthernTerritories -> english = "French Southern Territories"
            R.string.Gabon -> english = "Gabon"
            R.string.Gambia -> english = "Gambia"
            R.string.Georgia -> english = "Georgia"
            R.string.Germany -> english = "Germany"
            R.string.Ghana -> english = "Ghana"
            R.string.Gibraltar -> english = "Gibraltar"
            R.string.Greece -> english = "Greece"
            R.string.Greenland -> english = "Greenland"
            R.string.Grenada -> english = "Grenada"
            R.string.Guadeloupe -> english = "Guadeloupe"
            R.string.Guam -> english = "Guam"
            R.string.Guatemala -> english = "Guatemala"
            R.string.Guinea -> english = "Guinea"
            R.string.GuineaBissau -> english = "Guinea-Bissau"
            R.string.Guyana -> english = "Guyana"
            R.string.Haiti -> english = "Haiti"
            //R.string.HeardandMcDonaldIslands -> english = "Heard and Mc Donald Islands"
            //R.string.HolySee -> english = "Holy See"
            R.string.Honduras -> english = "Honduras"
            R.string.HongKong -> english = "Hong Kong"
            R.string.Hungary -> english = "Hungary"
            R.string.Iceland -> english = "Iceland"
            R.string.India -> english = "India"
            R.string.Indonesia -> english = "Indonesia"
            R.string.Iran -> english = "Iran"
            R.string.Iraq -> english = "Iraq"
            R.string.Ireland -> english = "Ireland"
            R.string.Israel -> english = "Israel"
            R.string.Italy -> english = "Italy"
            R.string.Jamaica -> english = "Jamaica"
            R.string.Japan -> english = "Japan"
            R.string.Jordan -> english = "Jordan"
            R.string.Kazakhstan -> english = "Kazakhstan"
            R.string.Kenya -> english = "Kenya"
            R.string.Kiribati -> english = "Kiribati"
            R.string.NorthKorea -> english = "North Korea"
            R.string.SouthKorea -> english = "South Korea"
            R.string.Kuwait -> english = "Kuwait"
            R.string.Kyrgyzstan -> english = "Kyrgyzstan"
            R.string.Laos -> english = "Laos"
            R.string.Latvia -> english = "Latvia"
            R.string.Lebanon -> english = "Lebanon"
            R.string.Lesotho -> english = "Lesotho"
            R.string.Liberia -> english = "Liberia"
            R.string.Libya -> english = "Libya"
            R.string.Liechtenstein -> english = "Liechtenstein"
            R.string.Lithuania -> english = "Lithuania"
            R.string.Luxembourg -> english = "Luxembourg"
            R.string.Macau -> english = "Macau"
            R.string.NorthMacedonia -> english = "North Macedonia"
            R.string.Madagascar -> english = "Madagascar"
            R.string.Malawi -> english = "Malawi"
            R.string.Malaysia -> english = "Malaysia"
            R.string.Maldives -> english = "Maldives"
            R.string.Mali -> english = "Mali"
            R.string.Malta -> english = "Malta"
            R.string.MarshallIslands -> english = "Marshall Islands"
            R.string.Martinique -> english = "Martinique"
            R.string.Mauritania -> english = "Mauritania"
            R.string.Mauritius -> english = "Mauritius"
            R.string.Mayotte -> english = "Mayotte"
            R.string.Mexico -> english = "Mexico"
            R.string.Micronesia -> english = "Micronesia"
            R.string.Moldova -> english = "Moldova"
            R.string.Monaco -> english = "Monaco"
            R.string.Mongolia -> english = "Mongolia"
            R.string.Montserrat -> english = "Montserrat"
            R.string.Montenegro -> english = "Montenegro"
            R.string.Morocco -> english = "Morocco"
            R.string.Mozambique -> english = "Mozambique"
            R.string.Myanmar -> english = "Myanmar"
            R.string.Namibia -> english = "Namibia"
            R.string.Nauru -> english = "Nauru"
            R.string.Nepal -> english = "Nepal"
            R.string.Netherlands -> english = "Netherlands"
            R.string.NetherlandsAntilles -> english = "Netherlands Antilles"
            R.string.NewCaledonia -> english = "New Caledonia"
            R.string.NewZealand -> english = "New Zealand"
            R.string.Nicaragua -> english = "Nicaragua"
            R.string.Niger -> english = "Niger"
            R.string.Nigeria -> english = "Nigeria"
            R.string.Niue -> english = "Niue"
            R.string.NorfolkIsland -> english = "Norfolk Island"
            R.string.NorthernMarianaIslands -> english = "Northern Mariana Islands"
            R.string.Norway -> english = "Norway"
            R.string.Oman -> english = "Oman"
            R.string.Pakistan -> english = "Pakistan"
            R.string.Palau -> english = "Palau"
            R.string.Panama -> english = "Panama"
            R.string.PapuaNewGuinea -> english = "Papua New Guinea"
            R.string.Paraguay -> english = "Paraguay"
            R.string.Peru -> english = "Peru"
            R.string.Philippines -> english = "Philippines"
            R.string.Pitcairn -> english = "Pitcairn"
            R.string.Poland -> english = "Poland"
            R.string.Portugal -> english = "Portugal"
            R.string.PuertoRico -> english = "Puerto Rico"
            R.string.Qatar -> english = "Qatar"
            R.string.Reunion -> english = "Reunion"
            R.string.Romania -> english = "Romania"
            R.string.Russia -> english = "Russia"
            R.string.Rwanda -> english = "Rwanda"
            R.string.SaintKittsandNevis -> english = "Saint Kitts and Nevis"
            R.string.SaintLucia -> english = "Saint Lucia"
            R.string.SaintVincentandtheGrenadines -> english = "Saint Vincent and the Grenadines"
            R.string.Samoa -> english = "Samoa"
            R.string.SanMarino -> english = "San Marino"
            //R.string.SaoTomeandPrincipe -> english = "Sao Tome and Principe"
            R.string.SaudiArabia -> english = "Saudi Arabia"
            R.string.Senegal -> english = "Senegal"
            R.string.Seychelles -> english = "Seychelles"
            R.string.SierraLeone -> english = "Sierra Leone"
            R.string.Singapore -> english = "Singapore"
            R.string.Slovakia -> english = "Slovakia"
            R.string.Slovenia -> english = "Slovenia"
            R.string.SolomonIslands -> english = "Solomon Islands"
            R.string.Somalia -> english = "Somalia"
            R.string.SouthAfrica -> english = "South Africa"
            R.string.Spain -> english = "Spain"
            R.string.SriLanka -> english = "Sri Lanka"
            R.string.StHelena -> english = "St. Helena"
            R.string.StPierreandMiquelon -> english = "St. Pierre and Miquelon"
            R.string.Sudan -> english = "Sudan"
            R.string.Suriname -> english = "Suriname"
            R.string.Swaziland -> english = "Swaziland"
            R.string.Sweden -> english = "Sweden"
            R.string.Switzerland -> english = "Switzerland"
            R.string.Syria -> english = "Syria"
            R.string.Taiwan -> english = "Taiwan"
            R.string.Tajikistan -> english = "Tajikistan"
            R.string.Tanzania -> english = "Tanzania"
            R.string.Thailand -> english = "Thailand"
            R.string.Togo -> english = "Togo"
            R.string.Tokelau -> english = "Tokelau"
            R.string.Tonga -> english = "Tonga"
            R.string.TrinidadandTobago -> english = "Trinidad and Tobago"
            R.string.Tunisia -> english = "Tunisia"
            R.string.Turkey -> english = "Turkey"
            R.string.Turkmenistan -> english = "Turkmenistan"
            R.string.TurksandCaicosIslands -> english = "Turks and Caicos Islands"
            R.string.Tuvalu -> english = "Tuvalu"
            R.string.Uganda -> english = "Uganda"
            R.string.Ukraine -> english = "Ukraine"
            R.string.UnitedArabEmirates -> english = "United Arab Emirates"
            R.string.UnitedKingdom -> english = "United Kingdom"
            R.string.UnitedStates -> english = "United States"
            R.string.Uruguay -> english = "Uruguay"
            R.string.Uzbekistan -> english = "Uzbekistan"
            R.string.Vanuatu -> english = "Vanuatu"
            R.string.Venezuela -> english = "Venezuela"
            R.string.Vietnam -> english = "Vietnam"
            R.string.VirginIslands -> english = "Virgin Islands"
            R.string.VirginIslands -> english = "Virgin Islands"
            R.string.WallisandFutunaIslands -> english = "Wallis and Futuna Islands"
            R.string.WesternSahara -> english = "Western Sahara"
            R.string.Yemen -> english = "Yemen"
            R.string.Zambia -> english = "Zambia"
            R.string.Zimbabwe -> english = "Zimbabwe"
            R.string.Palestine -> english = "Palestine"

        }
        return english
    }

//    fun findCountryByID(id: Int): Country{
//        for(c in world){
//            if(c.name == id){
//                return c
//            }
//        }
//        return world[1]
//    }

    fun printWorld(context: Context){
        val stringBuilder = StringBuilder()
        for(C in world){
            stringBuilder.append("\"${context.resources.getString(C.name)}\", ")
        }
        Log.i("World List", stringBuilder.toString())
    }


    fun fillFavourites(savedFavourites: ArrayList<Country>){
        favourites = savedFavourites
    }
    //Population source: https://www.worldometers.info/world-population/population-by-country/
    //TODO: check with right click refactor unused ressources for not used flags for countries that were commented out
    fun fillEurope(): List<Country> {
        val europe = ArrayList<Country>()
        europe.add(Country(R.drawable.al, R.string.Albania, 2877797))
//        europe.add(Country(R.drawable., "Andorra")) no flag
        europe.add(Country(R.drawable.at, R.string.Austria, 9006398))
        europe.add(Country(R.drawable.by, R.string.Belarus, 9449323))
        europe.add(Country(R.drawable.be, R.string.Belgium, 11589623))
        europe.add(Country(R.drawable.ba, R.string.BosniaandHerzegovina, 3280819))
        europe.add(Country(R.drawable.bg, R.string.Bulgaria, 6948445))
        europe.add(Country(R.drawable.hr, R.string.Croatia, 4105267))
        europe.add(Country(R.drawable.cz, R.string.CzechRepublic, 10708981))
        europe.add(Country(R.drawable.dk, R.string.Denmark, 5792202))
        europe.add(Country(R.drawable.ee, R.string.Estonia, 1326535))
        europe.add(Country(R.drawable.fi, R.string.Finland, 5450720))
        europe.add(Country(R.drawable.fr, R.string.France, 65273511))
        europe.add(Country(R.drawable.ger, R.string.Germany, 83783942))
        europe.add(Country(R.drawable.gr, R.string.Greece, 10423054))
        europe.add(Country(R.drawable.hu, R.string.Hungary, 9660351))
        europe.add(Country(R.drawable.ice, R.string.Iceland, 341243))
        europe.add(Country(R.drawable.ie, R.string.Ireland, 4937786))
        europe.add(Country(R.drawable.it, R.string.Italy, 60461826))
        europe.add(Country(R.drawable.lv, R.string.Latvia, 1886198))
        europe.add(Country(R.drawable.li, R.string.Liechtenstein, 38128))
        europe.add(Country(R.drawable.lt, R.string.Lithuania, 2722289))
        europe.add(Country(R.drawable.lu, R.string.Luxembourg, 625978))
        europe.add(Country(R.drawable.mt, R.string.Malta, 441543))
        europe.add(Country(R.drawable.md, R.string.Moldova, 4033963))
        europe.add(Country(R.drawable.mc, R.string.Monaco, 39242))
        europe.add(Country(R.drawable.me, R.string.Montenegro, 628066))
        europe.add(Country(R.drawable.nl, R.string.Netherlands, 17134872))
        europe.add(Country(R.drawable.mk, R.string.NorthMacedonia, 2083374))
        europe.add(Country(R.drawable.no, R.string.Norway, 5421241))
        europe.add(Country(R.drawable.pl, R.string.Poland, 37846611))
        europe.add(Country(R.drawable.pt, R.string.Portugal, 10196709))
        europe.add(Country(R.drawable.ro, R.string.Romania, 19237691))
        europe.add(Country(R.drawable.ru, R.string.Russia, 145934462))
        europe.add(Country(R.drawable.sm, R.string.SanMarino, 33931))
        europe.add(Country(R.drawable.rs, R.string.Serbia, 8737371))
        europe.add(Country(R.drawable.sk, R.string.Slovakia, 5459642))
        europe.add(Country(R.drawable.si, R.string.Slovenia, 2078938))
        europe.add(Country(R.drawable.es, R.string.Spain, 46754778))
        europe.add(Country(R.drawable.se, R.string.Sweden, 10099265))
        europe.add(Country(R.drawable.ch, R.string.Switzerland, 8654622))
        europe.add(Country(R.drawable.ua, R.string.Ukraine, 43733762))
        europe.add(Country(R.drawable.gb, R.string.UnitedKingdom, 67886011))



        return europe
    }

    fun fillAmericas(): List<Country>{
        val americas = ArrayList<Country>()
        americas.add(Country(R.drawable.ai,R.string.Anguilla,15003))
        americas.add(Country(R.drawable.ag,R.string.AntiguaandBarbuda,97929))
        americas.add(Country(R.drawable.ar,R.string.Argentina,45195774))
        americas.add(Country(R.drawable.aw,R.string.Aruba,106766))
        americas.add(Country(R.drawable.bs,R.string.Bahamas,393244))
        americas.add(Country(R.drawable.bb,R.string.Barbados,287375))
        americas.add(Country(R.drawable.bz,R.string.Belize,397375))
        americas.add(Country(R.drawable.bm,R.string.Bermuda,62278))
        americas.add(Country(R.drawable.bo,R.string.Bolivia,11673021))
        americas.add(Country(R.drawable.br,R.string.Brazil,212559417))
        americas.add(Country(R.drawable.vg,R.string.VirginIslands,30231))
        americas.add(Country(R.drawable.ca,R.string.Canada,37742154))
        //americas.add(Country(R.drawable.,"Caribbean Netherlands",26223,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.ky,R.string.CaymanIslands,65722))
        americas.add(Country(R.drawable.cl,R.string.Chile,19116201))
        americas.add(Country(R.drawable.co,R.string.Colombia,50882891))
        americas.add(Country(R.drawable.cr,R.string.CostaRica,5094118))
        americas.add(Country(R.drawable.cu,R.string.Cuba,11326616))
        //americas.add(Country(R.drawable.,"Curacao",164093,0,0,0,0,0,"2020-01-01",0,0,0))
        americas.add(Country(R.drawable.dm,R.string.Dominica,71986))
        americas.add(Country(R.drawable.dominican,R.string.DominicanRepublic,10847910))
        americas.add(Country(R.drawable.ec,R.string.Ecuador,17643054))
        americas.add(Country(R.drawable.sv,R.string.ElSalvador,6486205))
        americas.add(Country(R.drawable.fk,R.string.FalklandIslands,3480))
        americas.add(Country(R.drawable.gf,R.string.FrenchGuiana,298682))
        americas.add(Country(R.drawable.gl,R.string.Greenland,56770))
        americas.add(Country(R.drawable.gd,R.string.Grenada,112523))
        americas.add(Country(R.drawable.gp,R.string.Guadeloupe,400124))
        americas.add(Country(R.drawable.gy,R.string.Guyana,786552))
        americas.add(Country(R.drawable.ht,R.string.Haiti,11402528))
        americas.add(Country(R.drawable.hn,R.string.Honduras,9904607))
        americas.add(Country(R.drawable.jm,R.string.Jamaica,2961167))
        //check accuracy of flag
        americas.add(Country(R.drawable.mq,R.string.Martinique,375265))
        americas.add(Country(R.drawable.mx,R.string.Mexico,128932753))
        americas.add(Country(R.drawable.ms,R.string.Montserrat,4992))
        americas.add(Country(R.drawable.ni,R.string.Nicaragua,6624554))
        //check accuracy of flag
        americas.add(Country(R.drawable.pa,R.string.Panama,4314767))
        americas.add(Country(R.drawable.py,R.string.Paraguay,7132538))
        americas.add(Country(R.drawable.pe,R.string.Peru,32971854))
        americas.add(Country(R.drawable.pr,R.string.PuertoRico,2860853))
        americas.add(Country(R.drawable.kn,R.string.SaintKittsandNevis,53199))
        americas.add(Country(R.drawable.lc,R.string.SaintLucia,183627))
        //americas.add(Country(R.drawable.,"Saint Martin",38666,0,0,0,0,0,"2020-01-01",0,0,0))
//        americas.add(Country(R.drawable.pm,R.string.SaintPierreandMiquelon,5794))
        //americas.add(Country(R.drawable.,"Sint Marteen",42876,0,0,0,0,0,"2020-01-01",0,0,0))
//        americas.add(Country(R.drawable.vc,R.string.StVincentandtheGrenadines,110940))
        americas.add(Country(R.drawable.sr,R.string.Suriname,586632))
        americas.add(Country(R.drawable.tt,R.string.TrinidadandTobago,1399488))
        americas.add(Country(R.drawable.tc,R.string.TurksandCaicosIslands,38717))
        americas.add(Country(R.drawable.us,R.string.UnitedStates,331002651))
//        americas.add(Country(R.drawable.vi,"US Virgin Islands",104425))
        americas.add(Country(R.drawable.uy,R.string.Uruguay,3473730))
        americas.add(Country(R.drawable.ve,R.string.Venezuela,28435940))

        return americas
    }

    //TODO: Grenzfälle prüfen: Zypern, Türkei und Flaggen ergänzen
    fun fillAsia(): List<Country>{
        val asia = ArrayList<Country>()
        asia.add(Country(R.drawable.af,R.string.Afghanistan,38928346))
        asia.add(Country(R.drawable.am,R.string.Armenia,2963243))
        asia.add(Country(R.drawable.az,R.string.Azerbaijan,10139177))
        asia.add(Country(R.drawable.bh,R.string.Bahrain,1701575))
        asia.add(Country(R.drawable.bd,R.string.Bangladesh,164689383))
        asia.add(Country(R.drawable.bi,R.string.Bhutan,771608))
        asia.add(Country(R.drawable.bn,R.string.BruneiDarussalam,437479))
        asia.add(Country(R.drawable.kh,R.string.Cambodia,16718965))
        asia.add(Country(R.drawable.cn,R.string.China,1439323776))
        asia.add(Country(R.drawable.cy,R.string.Cyprus,1207359))
        asia.add(Country(R.drawable.ge,R.string.Georgia,3989167))
        asia.add(Country(R.drawable.hk,R.string.HongKong,7496981))
        asia.add(Country(R.drawable.india,R.string.India,1380004385))
        asia.add(Country(R.drawable.id,R.string.Indonesia,273523615))
        asia.add(Country(R.drawable.ir,R.string.Iran,83992949))
        asia.add(Country(R.drawable.iq,R.string.Iraq,40222493))
        asia.add(Country(R.drawable.il,R.string.Israel,8655535))
        asia.add(Country(R.drawable.jp,R.string.Japan,126476461))
        asia.add(Country(R.drawable.jo,R.string.Jordan,10203134))
        asia.add(Country(R.drawable.kz,R.string.Kazakhstan,18776707))
        asia.add(Country(R.drawable.kw,R.string.Kuwait,4270571))
        asia.add(Country(R.drawable.kg,R.string.Kyrgyzstan,6524195))
        asia.add(Country(R.drawable.la,R.string.Laos,7275560))
        asia.add(Country(R.drawable.lb,R.string.Lebanon,6825445))
        asia.add(Country(R.drawable.mo,R.string.Macau,649335)) //Macao ?
        asia.add(Country(R.drawable.my,R.string.Malaysia,32365999))
        asia.add(Country(R.drawable.mv,R.string.Maldives,540544))
        asia.add(Country(R.drawable.mn,R.string.Mongolia,3278290))
        asia.add(Country(R.drawable.mm,R.string.Myanmar,54409800))
        asia.add(Country(R.drawable.np,R.string.Nepal,29136808))
        asia.add(Country(R.drawable.kp,R.string.NorthKorea,25778816))
        asia.add(Country(R.drawable.om,R.string.Oman,5106626))
        asia.add(Country(R.drawable.pk,R.string.Pakistan,220892340))
        asia.add(Country(R.drawable.ph,R.string.Philippines,109581078))
        asia.add(Country(R.drawable.qa,R.string.Qatar,2881053))
        asia.add(Country(R.drawable.sa,R.string.SaudiArabia,34813871))
        asia.add(Country(R.drawable.sg,R.string.Singapore,5850342))
        asia.add(Country(R.drawable.kr,R.string.SouthKorea,51269185))
        asia.add(Country(R.drawable.lk,R.string.SriLanka,21413249))
        asia.add(Country(R.drawable.ps,R.string.Palestine,5101414)) //State of Palestine?
        asia.add(Country(R.drawable.sy,R.string.Syria,17500658))
        asia.add(Country(R.drawable.tw,R.string.Taiwan,23816775))
        asia.add(Country(R.drawable.tj,R.string.Tajikistan,9537645))
        asia.add(Country(R.drawable.th,R.string.Thailand,69799978))
        asia.add(Country(R.drawable.tl,R.string.EastTimor,1318445)) //Timor-Leste
        asia.add(Country(R.drawable.tr,R.string.Turkey,84339067))
        asia.add(Country(R.drawable.tm,R.string.Turkmenistan,6031200))
        asia.add(Country(R.drawable.ae,R.string.UnitedArabEmirates,9890402))
        asia.add(Country(R.drawable.uz,R.string.Uzbekistan,33469203))
        asia.add(Country(R.drawable.vn,R.string.Vietnam,97338579))
        asia.add(Country(R.drawable.ye,R.string.Yemen,29825964))


        return asia
    }

    fun fillAfrica(): List<Country>{
        val africa = ArrayList<Country>()
        africa.add(Country(R.drawable.dz,R.string.Algeria,43851044))
        africa.add(Country(R.drawable.ao,R.string.Angola,32866272))
        africa.add(Country(R.drawable.bj,R.string.Benin,12123200))
        africa.add(Country(R.drawable.bw,R.string.Botswana,2351627))
        africa.add(Country(R.drawable.bf,R.string.BurkinaFaso,20903273))
        africa.add(Country(R.drawable.bi,R.string.Burundi,11890784))
        africa.add(Country(R.drawable.cm,R.string.Cameroon,26545863))
        africa.add(Country(R.drawable.cv,R.string.CapeVerde,555987))
        africa.add(Country(R.drawable.cf,R.string.CentralAfricanRepublic,4829767))
        africa.add(Country(R.drawable.td,R.string.Chad,16425864))
        africa.add(Country(R.drawable.km,R.string.Comoros,869601))
        africa.add(Country(R.drawable.cg,R.string.Congo,5518087))
        africa.add(Country(R.drawable.ci,R.string.CoteDIvoire,26378274))
        africa.add(Country(R.drawable.dj,R.string.Djibouti,988000))
        africa.add(Country(R.drawable.cd,R.string.DRCongoe,89651403))
        africa.add(Country(R.drawable.eg,R.string.Egypt,102334404))
        africa.add(Country(R.drawable.gq,R.string.EquatorialGuinea,1402985))
        africa.add(Country(R.drawable.er,R.string.Eritrea,3546421))
        africa.add(Country(R.drawable.sz,R.string.Eswatini,1160164))
        africa.add(Country(R.drawable.et,R.string.Ethiopia,114963588))
        africa.add(Country(R.drawable.ga,R.string.Gabon,2225734))
        africa.add(Country(R.drawable.gm,R.string.Gambia,2416668))
        africa.add(Country(R.drawable.gh,R.string.Ghana,31072940))
        africa.add(Country(R.drawable.gn,R.string.Guinea,13132795))
        africa.add(Country(R.drawable.gw,R.string.GuineaBissau,1968001))
        africa.add(Country(R.drawable.ke,R.string.Kenya,53771296))
        africa.add(Country(R.drawable.ls,R.string.Lesotho,2142249))
        africa.add(Country(R.drawable.lr,R.string.Liberia,5057681))
        africa.add(Country(R.drawable.ly,R.string.Libya,6871292))
        africa.add(Country(R.drawable.mg,R.string.Madagascar,27691018))
        africa.add(Country(R.drawable.mw,R.string.Malawi,19129952))
        africa.add(Country(R.drawable.ml,R.string.Mali,20250833))
        africa.add(Country(R.drawable.mr,R.string.Mauritania,4649658))
        africa.add(Country(R.drawable.mu,R.string.Mauritius,1271768))
        africa.add(Country(R.drawable.yt,R.string.Mayotte,272815))
        africa.add(Country(R.drawable.ma,R.string.Morocco,36910560))
        africa.add(Country(R.drawable.mz,R.string.Mozambique,31255435))
        africa.add(Country(R.drawable.na,R.string.Namibia,2540905))
        africa.add(Country(R.drawable.ne,R.string.Niger,24206644))
        africa.add(Country(R.drawable.ng,R.string.Nigeria,206139589))
        africa.add(Country(R.drawable.rw,R.string.Rwanda,12952218))
        africa.add(Country(R.drawable.re,R.string.Reunion,895312))
        africa.add(Country(R.drawable.sh,R.string.StHelena,6077))
        //africa.add(Country(R.drawable.st,R.string.SaoTomeAndPrincipe,219159))
        africa.add(Country(R.drawable.sn,R.string.Senegal,16743927))
        africa.add(Country(R.drawable.sc,R.string.Seychelles,98347))
        africa.add(Country(R.drawable.sl,R.string.SierraLeone,7976983))
        africa.add(Country(R.drawable.so,R.string.Somalia,15893222))
        africa.add(Country(R.drawable.za,R.string.SouthAfrica,59308690))
//no flag
//africa.add(Country(R.drawable.ssd,"South Sudan",11193725,0,0,0,0,0,"2020-01-01",0,0,0))
        africa.add(Country(R.drawable.sd,R.string.Sudan,43849260))
        africa.add(Country(R.drawable.tz,R.string.Tanzania,59734218))
        africa.add(Country(R.drawable.tg,R.string.Togo,8278724))
        africa.add(Country(R.drawable.tn,R.string.Tunisia,11818619))
        africa.add(Country(R.drawable.ug,R.string.Uganda,45741007))
        africa.add(Country(R.drawable.eh,R.string.WesternSahara,597339))
        africa.add(Country(R.drawable.zm,R.string.Zambia,18383955))
        africa.add(Country(R.drawable.zw,R.string.Zimbabwe,14862924))


        return africa
    }

    fun fillOceania(): List<Country>{
        val oceania = ArrayList<Country>()
        //check flag
        oceania.add(Country(R.drawable.americansamoa,R.string.AmericanSamoa,55191))
        oceania.add(Country(R.drawable.au,R.string.Australia,25499884))
        oceania.add(Country(R.drawable.ck,R.string.CookIslands,17564))
        oceania.add(Country(R.drawable.fj,R.string.Fiji,896445))
        oceania.add(Country(R.drawable.pf,R.string.FrenchPolynesia,280908))
        oceania.add(Country(R.drawable.gu,R.string.Guam,168775))
        oceania.add(Country(R.drawable.ki,R.string.Kiribati,119449))
        oceania.add(Country(R.drawable.mh,R.string.MarshallIslands,59190))
        oceania.add(Country(R.drawable.fm,R.string.Micronesia,548914))
        oceania.add(Country(R.drawable.nr,R.string.Nauru,10824))
        oceania.add(Country(R.drawable.nc,R.string.NewCaledonia,2854980))
        oceania.add(Country(R.drawable.nz,R.string.NewZealand,4822233))
        oceania.add(Country(R.drawable.nu,R.string.Niue,1626))
        oceania.add(Country(R.drawable.mp,R.string.NorthernMarianaIslands,57559))
        oceania.add(Country(R.drawable.pw,R.string.Palau,18094))
        oceania.add(Country(R.drawable.pg,R.string.PapuaNewGuinea,8947024))
        oceania.add(Country(R.drawable.ws,R.string.Samoa,198414))
        oceania.add(Country(R.drawable.sb,R.string.SolomonIslands,686884))
        oceania.add(Country(R.drawable.tk,R.string.Tokelau,1357))
        oceania.add(Country(R.drawable.to,R.string.Tonga,105695))
        oceania.add(Country(R.drawable.tv,R.string.Tuvalu,11792))
        oceania.add(Country(R.drawable.vu,R.string.Vanuatu,307145))
        oceania.add(Country(R.drawable.wf,R.string.WallisandFutunaIslands,11239))

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