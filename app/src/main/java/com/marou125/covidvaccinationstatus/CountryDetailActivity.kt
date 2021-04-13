package com.marou125.covidvaccinationstatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.database.CountryRepository
import com.marou125.covidvaccinationstatus.service.CaseInfo
import com.marou125.covidvaccinationstatus.service.JsonCaseData
import com.marou125.covidvaccinationstatus.service.VaccinationData
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.Executors

class CountryDetailActivity : AppCompatActivity() {

    val countryRepository by lazy {
        CountryRepository.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        val country = intent.getStringExtra("country")
        if (country != null) {
            Log.i("The country is ", country)
        }

        //Find data for country in Singleton List
        val displayedCountryData : VaccinationData? = findCountryData(country!!)

        //Find data for country in Json Case Data object
        val displayedCountryCaseData = findCaseData(country!!)

        //Values were fetched and are
        if (displayedCountryData != null) {
            Log.i("Fetched data for", displayedCountryData.country)
        }

        //Fetch Country data from database
        val displayedCountry = countryRepository.getCountry(country)




        //Country CardView
        val flagIv = findViewById<ImageView>(R.id.detail_flag_iv)
        val nameTv = findViewById<TextView>(R.id.detail_country_name_tv)
        val populationTv = findViewById<TextView>(R.id.population_number_tv)
        val totalCasesTv = findViewById<TextView>(R.id.case_number_tv)
        val newCasesTv = findViewById<TextView>(R.id.new_cases_tv)
        val activeCasesTv = findViewById<TextView>(R.id.active_cases_number_tv)
        val deathsTv = findViewById<TextView>(R.id.total_deaths_number_tv)
        val newDeathsTv = findViewById<TextView>(R.id.new_deaths_number_tv)

        //Vaccination CardView
        val dateTv = findViewById<TextView>(R.id.date_data_tv)
        val totalVaccTv = findViewById<TextView>(R.id.total_vacc_number_tv)
        val peopleVaccTv = findViewById<TextView>(R.id.people_vacc_number_tv)
        val fullyVaccTv = findViewById<TextView>(R.id.people_full_vacc_number_tv)
        val vaccPercentageTv = findViewById<TextView>(R.id.vacc_percentage_tv)

        //Set data according to clicked country
        displayedCountry.observe(
            this,
            Observer { country ->
                val flag = country!!.flag
                val name = country.name
                val population = country.population
                val totalCases = country.totalCases
                val newCases = country.newCases
                val activeCases = country.activeCases
                val deaths = country.totalDeaths
                val newDeaths = country.newDeaths


                //Update country data to new values fetched from web
                if(displayedCountryData != null){
                    val arrayLength = displayedCountryData.data.size
                    val lastUpdate = displayedCountryData.data[arrayLength-1]

                    country.date = lastUpdate.date
                    country.totalVaccinations = if(lastUpdate.total_vaccinations != null) Integer.valueOf(lastUpdate.total_vaccinations) else country.totalVaccinations
                    country.firstVaccine = if(lastUpdate.people_vaccinated != null) Integer.valueOf(lastUpdate.people_vaccinated) else country.firstVaccine
                    country.fullyVaccinated = if(lastUpdate.people_fully_vaccinated != null) Integer.valueOf(lastUpdate.people_fully_vaccinated) else country.fullyVaccinated
                }
                //date missing
                val totalVaccinated = country.totalVaccinations
                val firstVaccine = country.firstVaccine
                val fullyVaccinated = country.fullyVaccinated

                flagIv.setImageResource(flag)
                nameTv.text = name
                populationTv.text = population.toString()
                totalCasesTv.text = totalCases.toString()
                newCasesTv.text = "+$newCases"
                activeCasesTv.text = activeCases.toString()
                deathsTv.text = deaths.toString()
                newDeathsTv.text = "+$newDeaths"

                dateTv.text = country.date
                totalVaccTv.text = if(totalVaccinated == 0) "No data" else formatNumber(totalVaccinated)
                peopleVaccTv.text = if(firstVaccine == 0) "No data" else formatNumber(firstVaccine)
                fullyVaccTv.text = if(fullyVaccinated == 0) "No data" else formatNumber(fullyVaccinated)

                //ProgressBar
                val percentage = fullyVaccinated / population.toDouble() * 100
                vaccPercentageTv.text = "$percentage fully vaccinated"

                val executor = Executors.newSingleThreadExecutor()
                executor.execute{
                    countryRepository.updateCountry(country)
                }

            }
        )
    }

    private fun formatNumber(number: Int): String{
        var numberString = number.toString()
        var formattedString = ""
        var counter = 1
        for(i in numberString.length-1 downTo 0){
            formattedString = numberString[i]+formattedString
            if(counter == 3 && i!=0){
                formattedString = ",$formattedString"
                counter = 0
            }
            counter++
        }
        return formattedString
    }

    private fun findCountryData(country: String): VaccinationData? {
            for(countryName in CountryDataSingleton.countryDataList){
                if (countryName.country.equals(country)){
                    return countryName
                }
            }
        return null
    }

    private fun findCaseData(country: String): CaseInfo? {
        val caseData = CountryDataSingleton.caseData
        var caseInfo: CaseInfo? = null
        when(country){
            "Afghanistan" -> caseInfo = caseData!!.Afghanistan.All
            "Albania" -> caseInfo = caseData!!.Albania.All
            "Algeria" -> caseInfo = caseData!!.Algeria.All
            "AmericanSamoa" -> caseInfo = caseData!!.AmericanSamoa.All
            "Andorra" -> caseInfo = caseData!!.Andorra.All
            "Angola" -> caseInfo = caseData!!.Angola.All
            "Anguilla" -> caseInfo = caseData!!.Anguilla.All
            "Antarctica" -> caseInfo = caseData!!.Antarctica.All
            "AntiguaAndBarbuda" -> caseInfo = caseData!!.AntiguaAndBarbuda.All
            "Argentina" -> caseInfo = caseData!!.Argentina.All
            "Armenia" -> caseInfo = caseData!!.Armenia.All
            "Aruba" -> caseInfo = caseData!!.Aruba.All
            "Australia" -> caseInfo = caseData!!.Australia.All
            "Austria" -> caseInfo = caseData!!.Austria.All
            "Azerbaijan" -> caseInfo = caseData!!.Azerbaijan.All
            "Bahamas" -> caseInfo = caseData!!.Bahamas.All
            "Bahrain" -> caseInfo = caseData!!.Bahrain.All
            "Bangladesh" -> caseInfo = caseData!!.Bangladesh.All
            "Barbados" -> caseInfo = caseData!!.Barbados.All
            "Belarus" -> caseInfo = caseData!!.Belarus.All
            "Belgium" -> caseInfo = caseData!!.Belgium.All
            "Belize" -> caseInfo = caseData!!.Belize.All
            "Benin" -> caseInfo = caseData!!.Benin.All
            "Bermuda" -> caseInfo = caseData!!.Bermuda.All
            "Bhutan" -> caseInfo = caseData!!.Bhutan.All
            "Bolivia" -> caseInfo = caseData!!.Bolivia.All
            "BosniaAndHerzegowina" -> caseInfo = caseData!!.BosniaAndHerzegowina.All
            "Botswana" -> caseInfo = caseData!!.Botswana.All
            "BouvetIsland" -> caseInfo = caseData!!.BouvetIsland.All
            "Brazil" -> caseInfo = caseData!!.Brazil.All
            "BritishIndianOceanTerritory" -> caseInfo = caseData!!.BritishIndianOceanTerritory.All
            "BruneiDarussalam" -> caseInfo = caseData!!.BruneiDarussalam.All
            "Bulgaria" -> caseInfo = caseData!!.Bulgaria.All
            "BurkinaFaso" -> caseInfo = caseData!!.BurkinaFaso.All
            "Burundi" -> caseInfo = caseData!!.Burundi.All
            "Cambodia" -> caseInfo = caseData!!.Cambodia.All
            "Cameroon" -> caseInfo = caseData!!.Cameroon.All
            "Canada" -> caseInfo = caseData!!.Canada.All
            "CapeVerde" -> caseInfo = caseData!!.CapeVerde.All
            "CaymanIslands" -> caseInfo = caseData!!.CaymanIslands.All
            "CentralAfricanRepublic" -> caseInfo = caseData!!.CentralAfricanRepublic.All
            "Chad" -> caseInfo = caseData!!.Chad.All
            "Chile" -> caseInfo = caseData!!.Chile.All
            "China" -> caseInfo = caseData!!.China.All
            "ChristmasIsland" -> caseInfo = caseData!!.ChristmasIsland.All
            "Cocos" -> caseInfo = caseData!!.Cocos.All
            "Colombia" -> caseInfo = caseData!!.Colombia.All
            "Comoros" -> caseInfo = caseData!!.Comoros.All
            "Congo" -> caseInfo = caseData!!.Congo.All
            "CongoTheDemocraticRepublicOfThe" -> caseInfo = caseData!!.CongoDR.All
            "CookIslands" -> caseInfo = caseData!!.CookIslands.All
            "CostaRica" -> caseInfo = caseData!!.CostaRica.All
            "CoteD'Ivoire" -> caseInfo = caseData!!.CoteDIvoire.All
            "Croatia" -> caseInfo = caseData!!.Croatia.All
            "Cuba" -> caseInfo = caseData!!.Cuba.All
            "Cyprus" -> caseInfo = caseData!!.Cyprus.All
            "CzechRepublic" -> caseInfo = caseData!!.CzechRepublic.All
            "Denmark" -> caseInfo = caseData!!.Denmark.All
            "Djibouti" -> caseInfo = caseData!!.Djibouti.All
            "Dominica" -> caseInfo = caseData!!.Dominica.All
            "DominicanRepublic" -> caseInfo = caseData!!.DominicanRepublic.All
            "EastTimor" -> caseInfo = caseData!!.EastTimor.All
            "Ecuador" -> caseInfo = caseData!!.Ecuador.All
            "Egypt" -> caseInfo = caseData!!.Egypt.All
            "ElSalvador" -> caseInfo = caseData!!.ElSalvador.All
            "EquatorialGuinea" -> caseInfo = caseData!!.EquatorialGuinea.All
            "Eritrea" -> caseInfo = caseData!!.Eritrea.All
            "Estonia" -> caseInfo = caseData!!.Estonia.All
            "Ethiopia" -> caseInfo = caseData!!.Ethiopia.All
            "FalklandIslands" -> caseInfo = caseData!!.FalklandIslands.All
            "FaroeIslands" -> caseInfo = caseData!!.FaroeIslands.All
            "Fiji" -> caseInfo = caseData!!.Fiji.All
            "Finland" -> caseInfo = caseData!!.Finland.All
            "France" -> caseInfo = caseData!!.France.All
            "FranceMetropolitan" -> caseInfo = caseData!!.FranceMetropolitan.All
            "FrenchGuiana" -> caseInfo = caseData!!.FrenchGuiana.All
            "FrenchPolynesia" -> caseInfo = caseData!!.FrenchPolynesia.All
            "FrenchSouthernTerritories" -> caseInfo = caseData!!.FrenchSouthernTerritories.All
            "Gabon" -> caseInfo = caseData!!.Gabon.All
            "Gambia" -> caseInfo = caseData!!.Gambia.All
            "Georgia" -> caseInfo = caseData!!.Georgia.All
            "Germany" -> caseInfo = caseData!!.Germany.All
            "Ghana" -> caseInfo = caseData!!.Ghana.All
            "Gibraltar" -> caseInfo = caseData!!.Gibraltar.All
            "Greece" -> caseInfo = caseData!!.Greece.All
            "Greenland" -> caseInfo = caseData!!.Greenland.All
            "Grenada" -> caseInfo = caseData!!.Grenada.All
            "Guadeloupe" -> caseInfo = caseData!!.Guadeloupe.All
            "Guam" -> caseInfo = caseData!!.Guam.All
            "Guatemala" -> caseInfo = caseData!!.Guatemala.All
            "Guinea" -> caseInfo = caseData!!.Guinea.All
            "Guinea-Bissau" -> caseInfo = caseData!!.GuineaBissau.All
            "Guyana" -> caseInfo = caseData!!.Guyana.All
            "Haiti" -> caseInfo = caseData!!.Haiti.All
            "HeardAndMcDonaldIslands" -> caseInfo = caseData!!.HeardAndMcDonaldIslands.All
            "HolySee" -> caseInfo = caseData!!.HolySee.All
            "Honduras" -> caseInfo = caseData!!.Honduras.All
            "HongKong" -> caseInfo = caseData!!.HongKong.All
            "Hungary" -> caseInfo = caseData!!.Hungary.All
            "Iceland" -> caseInfo = caseData!!.Iceland.All
            "India" -> caseInfo = caseData!!.India.All
            "Indonesia" -> caseInfo = caseData!!.Indonesia.All
            "Iran" -> caseInfo = caseData!!.Iran.All
            "Iraq" -> caseInfo = caseData!!.Iraq.All
            "Ireland" -> caseInfo = caseData!!.Ireland.All
            "Israel" -> caseInfo = caseData!!.Israel.All
            "Italy" -> caseInfo = caseData!!.Italy.All
            "Jamaica" -> caseInfo = caseData!!.Jamaica.All
            "Japan" -> caseInfo = caseData!!.Japan.All
            "Jordan" -> caseInfo = caseData!!.Jordan.All
            "Kazakhstan" -> caseInfo = caseData!!.Kazakhstan.All
            "Kenya" -> caseInfo = caseData!!.Kenya.All
            "Kiribati" -> caseInfo = caseData!!.Kiribati.All
            "KoreaDemocraticPeople'sRepublicOf" -> caseInfo = caseData!!.NorthKorea.All
            "KoreaRepublicOf" -> caseInfo = caseData!!.SouthKorea.All
            "Kuwait" -> caseInfo = caseData!!.Kuwait.All
            "Kyrgyzstan" -> caseInfo = caseData!!.Kyrgyzstan.All
            "LaoPeople'sDemocraticRepublic" -> caseInfo = caseData!!.Lao.All
            "Latvia" -> caseInfo = caseData!!.Latvia.All
            "Lebanon" -> caseInfo = caseData!!.Lebanon.All
            "Lesotho" -> caseInfo = caseData!!.Lesotho.All
            "Liberia" -> caseInfo = caseData!!.Liberia.All
            "LibyanArabJamahiriya" -> caseInfo = caseData!!.LibyanArabJamahiriya.All
            "Liechtenstein" -> caseInfo = caseData!!.Liechtenstein.All
            "Lithuania" -> caseInfo = caseData!!.Lithuania.All
            "Luxembourg" -> caseInfo = caseData!!.Luxembourg.All
            "Macau" -> caseInfo = caseData!!.Macau.All
            "MacedoniaTheFormerYugoslavRepublicOf" -> caseInfo = caseData!!.Macedonia.All
            "Madagascar" -> caseInfo = caseData!!.Madagascar.All
            "Malawi" -> caseInfo = caseData!!.Malawi.All
            "Malaysia" -> caseInfo = caseData!!.Malaysia.All
            "Maldives" -> caseInfo = caseData!!.Maldives.All
            "Mali" -> caseInfo = caseData!!.Mali.All
            "Malta" -> caseInfo = caseData!!.Malta.All
            "MarshallIslands" -> caseInfo = caseData!!.MarshallIslands.All
            "Martinique" -> caseInfo = caseData!!.Martinique.All
            "Mauritania" -> caseInfo = caseData!!.Mauritania.All
            "Mauritius" -> caseInfo = caseData!!.Mauritius.All
            "Mayotte" -> caseInfo = caseData!!.Mayotte.All
            "Mexico" -> caseInfo = caseData!!.Mexico.All
            "MicronesiaFederatedStatesOf" -> caseInfo = caseData!!.Micronesia.All
            "MoldovaRepublicOf" -> caseInfo = caseData!!.Moldova.All
            "Monaco" -> caseInfo = caseData!!.Monaco.All
            "Mongolia" -> caseInfo = caseData!!.Mongolia.All
            "Montserrat" -> caseInfo = caseData!!.Montserrat.All
            "Morocco" -> caseInfo = caseData!!.Morocco.All
            "Mozambique" -> caseInfo = caseData!!.Mozambique.All
            "Myanmar" -> caseInfo = caseData!!.Myanmar.All
            "Namibia" -> caseInfo = caseData!!.Namibia.All
            "Nauru" -> caseInfo = caseData!!.Nauru.All
            "Nepal" -> caseInfo = caseData!!.Nepal.All
            "Netherlands" -> caseInfo = caseData!!.Netherlands.All
            "NetherlandsAntilles" -> caseInfo = caseData!!.NetherlandsAntilles.All
            "NewCaledonia" -> caseInfo = caseData!!.NewCaledonia.All
            "NewZealand" -> caseInfo = caseData!!.NewZealand.All
            "Nicaragua" -> caseInfo = caseData!!.Nicaragua.All
            "Niger" -> caseInfo = caseData!!.Niger.All
            "Nigeria" -> caseInfo = caseData!!.Nigeria.All
            "Niue" -> caseInfo = caseData!!.Niue.All
            "NorfolkIsland" -> caseInfo = caseData!!.NorfolkIsland.All
            "NorthernMarianaIslands" -> caseInfo = caseData!!.NorthernMarianaIslands.All
            "Norway" -> caseInfo = caseData!!.Norway.All
            "Oman" -> caseInfo = caseData!!.Oman.All
            "Pakistan" -> caseInfo = caseData!!.Pakistan.All
            "Palau" -> caseInfo = caseData!!.Palau.All
            "Panama" -> caseInfo = caseData!!.Panama.All
            "PapuaNewGuinea" -> caseInfo = caseData!!.PapuaNewGuinea.All
            "Paraguay" -> caseInfo = caseData!!.Paraguay.All
            "Peru" -> caseInfo = caseData!!.Peru.All
            "Philippines" -> caseInfo = caseData!!.Philippines.All
            "Pitcairn" -> caseInfo = caseData!!.Pitcairn.All
            "Poland" -> caseInfo = caseData!!.Poland.All
            "Portugal" -> caseInfo = caseData!!.Portugal.All
            "PuertoRico" -> caseInfo = caseData!!.PuertoRico.All
            "Qatar" -> caseInfo = caseData!!.Qatar.All
            "Reunion" -> caseInfo = caseData!!.Reunion.All
            "Romania" -> caseInfo = caseData!!.Romania.All
            "RussianFederation" -> caseInfo = caseData!!.RussianFederation.All
            "Rwanda" -> caseInfo = caseData!!.Rwanda.All
            "SaintKittsAndNevis" -> caseInfo = caseData!!.SaintKittsAndNevis.All
            "SaintLucia" -> caseInfo = caseData!!.SaintLucia.All
            "SaintVincentAndTheGrenadines" -> caseInfo = caseData!!.SaintVincentAndTheGrenadines.All
            "Samoa" -> caseInfo = caseData!!.Samoa.All
            "SanMarino" -> caseInfo = caseData!!.SanMarino.All
            "SaoTomeAndPrincipe" -> caseInfo = caseData!!.SaoTomeAndPrincipe.All
            "SaudiArabia" -> caseInfo = caseData!!.SaudiArabia.All
            "Senegal" -> caseInfo = caseData!!.Senegal.All
            "Seychelles" -> caseInfo = caseData!!.Seychelles.All
            "SierraLeone" -> caseInfo = caseData!!.SierraLeone.All
            "Singapore" -> caseInfo = caseData!!.Singapore.All
            "Slovakia" -> caseInfo = caseData!!.Slovakia.All
            "Slovenia" -> caseInfo = caseData!!.Slovenia.All
            "SolomonIslands" -> caseInfo = caseData!!.SolomonIslands.All
            "Somalia" -> caseInfo = caseData!!.Somalia.All
            "SouthAfrica" -> caseInfo = caseData!!.SouthAfrica.All
            "SouthGeorgiaAndTheSouthSandwichIslands" -> caseInfo = caseData!!.SouthGeorgiaAndTheSouthSandwichIslands.All
            "Spain" -> caseInfo = caseData!!.Spain.All
            "SriLanka" -> caseInfo = caseData!!.SriLanka.All
            "StHelena" -> caseInfo = caseData!!.StHelena.All
            "StPierreAndMiquelon" -> caseInfo = caseData!!.StPierreAndMiquelon.All
            "Sudan" -> caseInfo = caseData!!.Sudan.All
            "Suriname" -> caseInfo = caseData!!.Suriname.All
            "SvalbardAndJanMayenIslands" -> caseInfo = caseData!!.SvalbardAndJanMayenIslands.All
            "Swaziland" -> caseInfo = caseData!!.Swaziland.All
            "Sweden" -> caseInfo = caseData!!.Sweden.All
            "Switzerland" -> caseInfo = caseData!!.Switzerland.All
            "SyrianArabRepublic" -> caseInfo = caseData!!.SyrianArabRepublic.All
            "TaiwanProvinceOfChina" -> caseInfo = caseData!!.Taiwan.All
            "Tajikistan" -> caseInfo = caseData!!.Tajikistan.All
            "TanzaniaUnitedRepublicOf" -> caseInfo = caseData!!.Tanzania.All
            "Thailand" -> caseInfo = caseData!!.Thailand.All
            "Togo" -> caseInfo = caseData!!.Togo.All
            "Tokelau" -> caseInfo = caseData!!.Tokelau.All
            "Tonga" -> caseInfo = caseData!!.Tonga.All
            "TrinidadAndTobago" -> caseInfo = caseData!!.TrinidadAndTobago.All
            "Tunisia" -> caseInfo = caseData!!.Tunisia.All
            "Turkey" -> caseInfo = caseData!!.Turkey.All
            "Turkmenistan" -> caseInfo = caseData!!.Turkmenistan.All
            "TurksAndCaicosIslands" -> caseInfo = caseData!!.TurksAndCaicosIslands.All
            "Tuvalu" -> caseInfo = caseData!!.Tuvalu.All
            "Uganda" -> caseInfo = caseData!!.Uganda.All
            "Ukraine" -> caseInfo = caseData!!.Ukraine.All
            "UnitedArabEmirates" -> caseInfo = caseData!!.UnitedArabEmirates.All
            "UnitedKingdom" -> caseInfo = caseData!!.UnitedKingdom.All
            "UnitedStates" -> caseInfo = caseData!!.UnitedStates.All
            "UnitedStatesMinorOutlyingIslands" -> caseInfo = caseData!!.UnitedStatesMinorOutlyingIslands.All
            "Uruguay" -> caseInfo = caseData!!.Uruguay.All
            "Uzbekistan" -> caseInfo = caseData!!.Uzbekistan.All
            "Vanuatu" -> caseInfo = caseData!!.Vanuatu.All
            "Venezuela" -> caseInfo = caseData!!.Venezuela.All
            "Vietnam" -> caseInfo = caseData!!.Vietnam.All
            "VirginIslands" -> caseInfo = caseData!!.VirginIslandsUK.All
            "VirginIslands" -> caseInfo = caseData!!.VirginIslandsUS.All
            "WallisAndFutunaIslands" -> caseInfo = caseData!!.WallisAndFutunaIslands.All
            "WesternSahara" -> caseInfo = caseData!!.WesternSahara.All
            "Yemen" -> caseInfo = caseData!!.Yemen.All
            "Yugoslavia" -> caseInfo = caseData!!.Yugoslavia.All
            "Zambia" -> caseInfo = caseData!!.Zambia.All
            "Zimbabwe" -> caseInfo = caseData!!.Zimbabwe.All
            "Palestine" -> caseInfo = caseData!!.Palestine.All


        }
        return caseInfo
    }

    //onStop newly fetched Data has to be inserted into database
    override fun onStop() {
        Log.i("DATA SAVED" , "YES")
        super.onStop()
    }
}