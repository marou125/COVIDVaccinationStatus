package com.marou125.covidvaccinationstatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.marou125.covidvaccinationstatus.database.CountryRepository
import com.marou125.covidvaccinationstatus.databinding.ActivityCountryDetailBinding
import com.marou125.covidvaccinationstatus.service.CaseInfo
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.util.concurrent.Executors
import kotlin.math.floor

class CountryDetailActivity : AppCompatActivity() {

    val countryRepository by lazy {
        CountryRepository.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityCountryDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_country_detail)

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


        //Set data according to clicked country
        displayedCountry.observe(
            this,
            Observer { country ->


                //Update country data to new values fetched from web
                if(displayedCountryData != null){
                    val arrayLength = displayedCountryData.data.size
                    val lastUpdate = displayedCountryData.data[arrayLength-1]

                    country!!.date = lastUpdate.date
                    country.totalVaccinations = if(lastUpdate.total_vaccinations != null) Integer.valueOf(lastUpdate.total_vaccinations) else country.totalVaccinations
                    country.firstVaccine = if(lastUpdate.people_vaccinated != null) Integer.valueOf(lastUpdate.people_vaccinated) else country.firstVaccine
                    country.fullyVaccinated = if(lastUpdate.people_fully_vaccinated != null) Integer.valueOf(lastUpdate.people_fully_vaccinated) else country.fullyVaccinated
                }

                /*TODO:New Cases and New Deaths are not shown correctly because the total values of the day before are not saved anywhere and the population data seems inaccurate*/
                if(displayedCountryCaseData != null){
                    country!!.population = displayedCountryCaseData.population
                    country.newDeaths = displayedCountryCaseData.deaths - country.totalDeaths
                    country.totalDeaths = displayedCountryCaseData.deaths
                    country.newCases = displayedCountryCaseData.confirmed - country.totalCases
                    country.totalCases = displayedCountryCaseData.confirmed
                    country.activeCases = displayedCountryCaseData.confirmed - displayedCountryCaseData.recovered
                }

                binding.detailFlagIv.setImageResource(country!!.flag)
                binding.detailCountryNameTv.text = country.name
                binding.populationNumberTv.text = formatNumber(country.population)
                binding.caseNumberTv.text = formatNumber(country.totalCases)
                binding.newCasesTv.text = "+${country.newCases}"
                binding.activeCasesNumberTv.text = formatNumber(country.activeCases)
                binding.totalDeathsNumberTv.text = formatNumber(country.totalDeaths)
                binding.newDeathsNumberTv.text = "+${country.newDeaths}"

                binding.dateDataTv.text = country.date
                binding.totalVaccNumberTv.text = if(country.totalVaccinations == 0) "No data" else formatNumber(country.totalVaccinations)
                binding.peopleVaccNumberTv.text = if(country.firstVaccine == 0) "No data" else formatNumber(country.firstVaccine)
                binding.peopleFullVaccNumberTv.text = if(country.fullyVaccinated == 0) "No data" else formatNumber(country.fullyVaccinated)

                //ProgressBar
                if(binding.peopleFullVaccNumberTv.text.equals("No data")){
                    binding.progressBarFully.visibility = View.GONE
                    binding.vaccPercentageFullTv.visibility = View.GONE
                }
                val fullyVaccinatedPercentage = calculatePercentage(country.fullyVaccinated, country.population)
                binding.vaccPercentageFullTv.text = "$fullyVaccinatedPercentage % is fully vaccinated"
                binding.progressBarFully.progress = fullyVaccinatedPercentage.toInt()

                if(binding.peopleVaccNumberTv.text.equals("No data")){
                    binding.progressBarFirst.visibility = View.GONE
                    binding.vaccPercentageFirstTv.visibility = View.GONE
                }
                val firstVaccinePercentage = calculatePercentage(country.firstVaccine, country.population)
                binding.vaccPercentageFirstTv.text = "$firstVaccinePercentage % with at least one vaccine shot"
                binding.progressBarFirst.progress = firstVaccinePercentage.toInt()


                val executor = Executors.newSingleThreadExecutor()
                executor.execute{
                    countryRepository.updateCountry(country)
                }

            }
        )
    }

    private fun calculatePercentage(vaccinated: Int, population: Int): Double = floor((vaccinated/population.toDouble()) * 1000) /10


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
            "American Samoa" -> caseInfo = caseData!!.AmericanSamoa.All
            "Andorra" -> caseInfo = caseData!!.Andorra.All
            "Angola" -> caseInfo = caseData!!.Angola.All
            "Anguilla" -> caseInfo = caseData!!.Anguilla.All
            "Antarctica" -> caseInfo = caseData!!.Antarctica.All
            "Antigua and Barbuda" -> caseInfo = caseData!!.AntiguaAndBarbuda.All
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
            "Bosnia and Herzegovina" -> caseInfo = caseData!!.BosniaAndHerzegovina.All
            "Botswana" -> caseInfo = caseData!!.Botswana.All
            "Bouvet Island" -> caseInfo = caseData!!.BouvetIsland.All
            "Brazil" -> caseInfo = caseData!!.Brazil.All
            "British Indian Ocean Territory" -> caseInfo = caseData!!.BritishIndianOceanTerritory.All
            "Brunei" -> caseInfo = caseData!!.BruneiDarussalam.All
            "Bulgaria" -> caseInfo = caseData!!.Bulgaria.All
            "Burkina Faso" -> caseInfo = caseData!!.BurkinaFaso.All
            "Burundi" -> caseInfo = caseData!!.Burundi.All
            "Cambodia" -> caseInfo = caseData!!.Cambodia.All
            "Cameroon" -> caseInfo = caseData!!.Cameroon.All
            "Canada" -> caseInfo = caseData!!.Canada.All
            "Cape Verde" -> caseInfo = caseData!!.CapeVerde.All
            "Cayman Islands" -> caseInfo = caseData!!.CaymanIslands.All
            "Central African Republic" -> caseInfo = caseData!!.CentralAfricanRepublic.All
            "Chad" -> caseInfo = caseData!!.Chad.All
            "Chile" -> caseInfo = caseData!!.Chile.All
            "China" -> caseInfo = caseData!!.China.All
            "Christmas Island" -> caseInfo = caseData!!.ChristmasIsland.All
            "Cocos" -> caseInfo = caseData!!.Cocos.All
            "Colombia" -> caseInfo = caseData!!.Colombia.All
            "Comoros" -> caseInfo = caseData!!.Comoros.All
            "Congo" -> caseInfo = caseData!!.Congo.All
            "Congo The Democratic Republic Of The" -> caseInfo = caseData!!.CongoDR.All
            "Cook Islands" -> caseInfo = caseData!!.CookIslands.All
            "CostaRica" -> caseInfo = caseData!!.CostaRica.All
            "Cote D'Ivoire" -> caseInfo = caseData!!.CoteDIvoire.All
            "Croatia" -> caseInfo = caseData!!.Croatia.All
            "Cuba" -> caseInfo = caseData!!.Cuba.All
            "Cyprus" -> caseInfo = caseData!!.Cyprus.All
            "Czechia" -> caseInfo = caseData!!.CzechRepublic.All
            "Denmark" -> caseInfo = caseData!!.Denmark.All
            "Djibouti" -> caseInfo = caseData!!.Djibouti.All
            "Dominica" -> caseInfo = caseData!!.Dominica.All
            "Dominican Republic" -> caseInfo = caseData!!.DominicanRepublic.All
            "East Timor" -> caseInfo = caseData!!.EastTimor.All
            "Ecuador" -> caseInfo = caseData!!.Ecuador.All
            "Egypt" -> caseInfo = caseData!!.Egypt.All
            "El Salvador" -> caseInfo = caseData!!.ElSalvador.All
            "Equatorial Guinea" -> caseInfo = caseData!!.EquatorialGuinea.All
            "Eritrea" -> caseInfo = caseData!!.Eritrea.All
            "Estonia" -> caseInfo = caseData!!.Estonia.All
            "Ethiopia" -> caseInfo = caseData!!.Ethiopia.All
            "Falkland Islands" -> caseInfo = caseData!!.FalklandIslands.All
            "Faroe Islands" -> caseInfo = caseData!!.FaroeIslands.All
            "Fiji" -> caseInfo = caseData!!.Fiji.All
            "Finland" -> caseInfo = caseData!!.Finland.All
            "France" -> caseInfo = caseData!!.France.All
            "France Metropolitan" -> caseInfo = caseData!!.FranceMetropolitan.All
            "French Guiana" -> caseInfo = caseData!!.FrenchGuiana.All
            "French Polynesia" -> caseInfo = caseData!!.FrenchPolynesia.All
            "French Southern Territories" -> caseInfo = caseData!!.FrenchSouthernTerritories.All
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
            "Heard and McDonald Islands" -> caseInfo = caseData!!.HeardAndMcDonaldIslands.All
            "Holy See" -> caseInfo = caseData!!.HolySee.All
            "Honduras" -> caseInfo = caseData!!.Honduras.All
            "Hong Kong" -> caseInfo = caseData!!.HongKong.All
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
            "North Korea" -> caseInfo = caseData!!.NorthKorea.All
            "South Korea" -> caseInfo = caseData!!.SouthKorea.All
            "Kuwait" -> caseInfo = caseData!!.Kuwait.All
            "Kyrgyzstan" -> caseInfo = caseData!!.Kyrgyzstan.All
            "Lao" -> caseInfo = caseData!!.Lao.All
            "Latvia" -> caseInfo = caseData!!.Latvia.All
            "Lebanon" -> caseInfo = caseData!!.Lebanon.All
            "Lesotho" -> caseInfo = caseData!!.Lesotho.All
            "Liberia" -> caseInfo = caseData!!.Liberia.All
            "Libyan Arab Jamahiriya" -> caseInfo = caseData!!.LibyanArabJamahiriya.All
            "Liechtenstein" -> caseInfo = caseData!!.Liechtenstein.All
            "Lithuania" -> caseInfo = caseData!!.Lithuania.All
            "Luxembourg" -> caseInfo = caseData!!.Luxembourg.All
            "Macau" -> caseInfo = caseData!!.Macau.All
            "Macedonia" -> caseInfo = caseData!!.Macedonia.All
            "Madagascar" -> caseInfo = caseData!!.Madagascar.All
            "Malawi" -> caseInfo = caseData!!.Malawi.All
            "Malaysia" -> caseInfo = caseData!!.Malaysia.All
            "Maldives" -> caseInfo = caseData!!.Maldives.All
            "Mali" -> caseInfo = caseData!!.Mali.All
            "Malta" -> caseInfo = caseData!!.Malta.All
            "Marshall Islands" -> caseInfo = caseData!!.MarshallIslands.All
            "Martinique" -> caseInfo = caseData!!.Martinique.All
            "Mauritania" -> caseInfo = caseData!!.Mauritania.All
            "Mauritius" -> caseInfo = caseData!!.Mauritius.All
            "Mayotte" -> caseInfo = caseData!!.Mayotte.All
            "Mexico" -> caseInfo = caseData!!.Mexico.All
            "Micronesia" -> caseInfo = caseData!!.Micronesia.All
            "Moldova" -> caseInfo = caseData!!.Moldova.All
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
            "Netherlands Antilles" -> caseInfo = caseData!!.NetherlandsAntilles.All
            "New Caledonia" -> caseInfo = caseData!!.NewCaledonia.All
            "New Zealand" -> caseInfo = caseData!!.NewZealand.All
            "Nicaragua" -> caseInfo = caseData!!.Nicaragua.All
            "Niger" -> caseInfo = caseData!!.Niger.All
            "Nigeria" -> caseInfo = caseData!!.Nigeria.All
            "Niue" -> caseInfo = caseData!!.Niue.All
            "Norfolk Island" -> caseInfo = caseData!!.NorfolkIsland.All
            "Northern Mariana Islands" -> caseInfo = caseData!!.NorthernMarianaIslands.All
            "Norway" -> caseInfo = caseData!!.Norway.All
            "Oman" -> caseInfo = caseData!!.Oman.All
            "Pakistan" -> caseInfo = caseData!!.Pakistan.All
            "Palau" -> caseInfo = caseData!!.Palau.All
            "Panama" -> caseInfo = caseData!!.Panama.All
            "Papua New Guinea" -> caseInfo = caseData!!.PapuaNewGuinea.All
            "Paraguay" -> caseInfo = caseData!!.Paraguay.All
            "Peru" -> caseInfo = caseData!!.Peru.All
            "Philippines" -> caseInfo = caseData!!.Philippines.All
            "Pitcairn" -> caseInfo = caseData!!.Pitcairn.All
            "Poland" -> caseInfo = caseData!!.Poland.All
            "Portugal" -> caseInfo = caseData!!.Portugal.All
            "Puerto Rico" -> caseInfo = caseData!!.PuertoRico.All
            "Qatar" -> caseInfo = caseData!!.Qatar.All
            "Reunion" -> caseInfo = caseData!!.Reunion.All
            "Romania" -> caseInfo = caseData!!.Romania.All
            "Russian Federation" -> caseInfo = caseData!!.RussianFederation.All
            "Rwanda" -> caseInfo = caseData!!.Rwanda.All
            "Saint Kitts and Nevis" -> caseInfo = caseData!!.SaintKittsAndNevis.All
            "SaintLucia" -> caseInfo = caseData!!.SaintLucia.All
            "Saint Vincent and the Grenadines" -> caseInfo = caseData!!.SaintVincentAndTheGrenadines.All
            "Samoa" -> caseInfo = caseData!!.Samoa.All
            "San Marino" -> caseInfo = caseData!!.SanMarino.All
            "Sao Tome and Principe" -> caseInfo = caseData!!.SaoTomeAndPrincipe.All
            "Saudi Arabia" -> caseInfo = caseData!!.SaudiArabia.All
            "Senegal" -> caseInfo = caseData!!.Senegal.All
            "Seychelles" -> caseInfo = caseData!!.Seychelles.All
            "SierraLeone" -> caseInfo = caseData!!.SierraLeone.All
            "Singapore" -> caseInfo = caseData!!.Singapore.All
            "Slovakia" -> caseInfo = caseData!!.Slovakia.All
            "Slovenia" -> caseInfo = caseData!!.Slovenia.All
            "Solomon Islands" -> caseInfo = caseData!!.SolomonIslands.All
            "Somalia" -> caseInfo = caseData!!.Somalia.All
            "South Africa" -> caseInfo = caseData!!.SouthAfrica.All
            "South Georgia and the South Sandwich Islands" -> caseInfo = caseData!!.SouthGeorgiaAndTheSouthSandwichIslands.All
            "Spain" -> caseInfo = caseData!!.Spain.All
            "Sri Lanka" -> caseInfo = caseData!!.SriLanka.All
            "St Helena" -> caseInfo = caseData!!.StHelena.All
            "St Pierre and Miquelon" -> caseInfo = caseData!!.StPierreAndMiquelon.All
            "Sudan" -> caseInfo = caseData!!.Sudan.All
            "Suriname" -> caseInfo = caseData!!.Suriname.All
            "Svalbard and Jan Mayen Islands" -> caseInfo = caseData!!.SvalbardAndJanMayenIslands.All
            "Swaziland" -> caseInfo = caseData!!.Swaziland.All
            "Sweden" -> caseInfo = caseData!!.Sweden.All
            "Switzerland" -> caseInfo = caseData!!.Switzerland.All
            "Syrian Arab Republic" -> caseInfo = caseData!!.SyrianArabRepublic.All
            "Taiwan Province of China" -> caseInfo = caseData!!.Taiwan.All
            "Tajikistan" -> caseInfo = caseData!!.Tajikistan.All
            "Tanzania" -> caseInfo = caseData!!.Tanzania.All
            "Thailand" -> caseInfo = caseData!!.Thailand.All
            "Togo" -> caseInfo = caseData!!.Togo.All
            "Tokelau" -> caseInfo = caseData!!.Tokelau.All
            "Tonga" -> caseInfo = caseData!!.Tonga.All
            "Trinidad and Tobago" -> caseInfo = caseData!!.TrinidadAndTobago.All
            "Tunisia" -> caseInfo = caseData!!.Tunisia.All
            "Turkey" -> caseInfo = caseData!!.Turkey.All
            "Turkmenistan" -> caseInfo = caseData!!.Turkmenistan.All
            "TurksAndCaicosIslands" -> caseInfo = caseData!!.TurksAndCaicosIslands.All
            "Tuvalu" -> caseInfo = caseData!!.Tuvalu.All
            "Uganda" -> caseInfo = caseData!!.Uganda.All
            "Ukraine" -> caseInfo = caseData!!.Ukraine.All
            "United Arab Emirates" -> caseInfo = caseData!!.UnitedArabEmirates.All
            "United Kingdom" -> caseInfo = caseData!!.UnitedKingdom.All
            "United States" -> caseInfo = caseData!!.UnitedStates.All
            "UnitedStatesMinorOutlyingIslands" -> caseInfo = caseData!!.UnitedStatesMinorOutlyingIslands.All
            "Uruguay" -> caseInfo = caseData!!.Uruguay.All
            "Uzbekistan" -> caseInfo = caseData!!.Uzbekistan.All
            "Vanuatu" -> caseInfo = caseData!!.Vanuatu.All
            "Venezuela" -> caseInfo = caseData!!.Venezuela.All
            "Vietnam" -> caseInfo = caseData!!.Vietnam.All
            "Virgin Islands UK" -> caseInfo = caseData!!.VirginIslandsUK.All
            "Virgin Islands US" -> caseInfo = caseData!!.VirginIslandsUS.All
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