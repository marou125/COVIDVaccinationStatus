package com.marou125.covidvaccinationstatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.marou125.covidvaccinationstatus.database.CountryRepository
import com.marou125.covidvaccinationstatus.databinding.ActivityCountryDetailBinding
import com.marou125.covidvaccinationstatus.service.CaseInfo
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.floor
import kotlin.reflect.KFunction

//TODO: Refactoring, flag appears small on some countries, create viewmodel for this class
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
        val caseInfoArray = findCaseData(country!!)
        val displayedCountryCaseData = caseInfoArray[0]

        //Get last recorded seven days of cases and deaths saved into an Array
        val lastWeekCases = CountryDataSingleton.getWeeklyData(caseInfoArray[1]!!.dates)
        val lastWeekDeaths = CountryDataSingleton.getWeeklyData(caseInfoArray[2]!!.dates)

        val newCases = lastWeekCases[0] - lastWeekCases[1]
        val newDeaths = lastWeekDeaths[0] - lastWeekDeaths[1]


        Log.i("LAST WEEK CASES in $country", Arrays.toString(lastWeekCases))
        Log.i("LAST WEEK DEATHS in $country", Arrays.toString(lastWeekDeaths))

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
                    country.sevenDayAverage = if(lastUpdate.daily_vaccinations != null) Integer.valueOf(lastUpdate.daily_vaccinations) else country.sevenDayAverage
                }

                /*TODO:New Cases and New Deaths are not shown correctly because the total values of the day before are not saved anywhere
                *  Idea: Use the case numbers from the day before to determine new cases and new deaths*/
                if(displayedCountryCaseData != null && displayedCountryCaseData.confirmed != country!!.totalCases){
                    country!!.newDeaths = newDeaths
                    country.totalDeaths = displayedCountryCaseData.deaths
                    country.newCases = newCases
                    country.totalCases = displayedCountryCaseData.confirmed
                    //The active cases are the confirmed cases subtracted by the recovered people and people who passed away
                    country.activeCases = displayedCountryCaseData.confirmed - displayedCountryCaseData.recovered - country.totalDeaths

                }

                val detailFlagIV = findViewById<ImageView>(R.id.detail_flag_iv)
                val detailCountryNameTV = findViewById<TextView>(R.id.detail_country_name_tv)
                detailFlagIV.setImageResource(country!!.flag)
                detailCountryNameTV.text = country.name
                binding.populationNumberTv.text = formatNumber(country.population)
                binding.caseNumberTv.text = formatNumber(country.totalCases)
                binding.newCasesTv.text = "(+${formatNumber(country.newCases)})"
                binding.activeCasesNumberTv.text = formatNumber(country.activeCases)
                binding.totalDeathsNumberTv.text = formatNumber(country.totalDeaths)
                binding.newDeathsNumberTv.text = "(+${formatNumber(country.newDeaths)})"

                binding.dateDataTv.text = country.date
                binding.totalVaccNumberTv.text = if(country.totalVaccinations == 0) "No data" else formatNumber(country.totalVaccinations)
                binding.peopleVaccNumberTv.text = if(country.firstVaccine == 0) "No data" else formatNumber(country.firstVaccine)
                binding.peopleFullVaccNumberTv.text = if(country.fullyVaccinated == 0) "No data" else formatNumber(country.fullyVaccinated)
                binding.sevenDayAvgNumber.text = if(country.sevenDayAverage == 0) "No data" else formatNumber(country.sevenDayAverage)

                //ProgressBar
                if(binding.peopleFullVaccNumberTv.text.equals("No data")){
                    binding.progressBarFully.visibility = View.GONE
                    binding.vaccPercentageFullTv.visibility = View.GONE
                }
                val fullyVaccinatedPercentage = calculatePercentage(country.fullyVaccinated, country.population)
                binding.vaccPercentageFullTv.text = "$fullyVaccinatedPercentage % are fully vaccinated"
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


    //Formats a number into an easily readable string (Ex: 5000000 to 5,000,000)
    private fun formatNumber(number: Int): String{
        if(number == 0){
            return "0"
        }
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

    //TODO: fix switch case country names
    private fun findCaseData(country: String): Array<CaseInfo?> {
        val caseData = CountryDataSingleton.caseData
        val historicCases = CountryDataSingleton.historicCases
        val historicDeaths = CountryDataSingleton.historicDeaths
        var caseInfo: Array<CaseInfo?> = emptyArray()
        when(country){
            "Afghanistan" -> caseInfo = arrayOf(caseData!!.Afghanistan.All, historicCases!!.Afghanistan.All, historicDeaths!!.Afghanistan.All)
            "Albania" -> caseInfo = arrayOf(caseData!!.Albania.All, historicCases!!.Albania.All, historicDeaths!!.Albania.All)
            "Algeria" -> caseInfo = arrayOf(caseData!!.Algeria.All, historicCases!!.Algeria.All, historicDeaths!!.Algeria.All)
            "AmericanSamoa" -> caseInfo = arrayOf(caseData!!.AmericanSamoa.All, historicCases!!.AmericanSamoa.All, historicDeaths!!.AmericanSamoa.All)
            "Andorra" -> caseInfo = arrayOf(caseData!!.Andorra.All, historicCases!!.Andorra.All, historicDeaths!!.Andorra.All)
            "Angola" -> caseInfo = arrayOf(caseData!!.Angola.All, historicCases!!.Angola.All, historicDeaths!!.Angola.All)
            "Anguilla" -> caseInfo = arrayOf(caseData!!.Anguilla.All, historicCases!!.Anguilla.All, historicDeaths!!.Anguilla.All)
            "Antarctica" -> caseInfo = arrayOf(caseData!!.Antarctica.All, historicCases!!.Antarctica.All, historicDeaths!!.Antarctica.All)
            "Antigua and Barbuda" -> caseInfo = arrayOf(caseData!!.AntiguaAndBarbuda.All, historicCases!!.AntiguaAndBarbuda.All, historicDeaths!!.AntiguaAndBarbuda.All)
            "Argentina" -> caseInfo = arrayOf(caseData!!.Argentina.All, historicCases!!.Argentina.All, historicDeaths!!.Argentina.All)
            "Armenia" -> caseInfo = arrayOf(caseData!!.Armenia.All, historicCases!!.Armenia.All, historicDeaths!!.Armenia.All)
            "Aruba" -> caseInfo = arrayOf(caseData!!.Aruba.All, historicCases!!.Aruba.All, historicDeaths!!.Aruba.All)
            "Australia" -> caseInfo = arrayOf(caseData!!.Australia.All, historicCases!!.Australia.All, historicDeaths!!.Australia.All)
            "Austria" -> caseInfo = arrayOf(caseData!!.Austria.All, historicCases!!.Austria.All, historicDeaths!!.Austria.All)
            "Azerbaijan" -> caseInfo = arrayOf(caseData!!.Azerbaijan.All, historicCases!!.Azerbaijan.All, historicDeaths!!.Azerbaijan.All)
            "Bahamas" -> caseInfo = arrayOf(caseData!!.Bahamas.All, historicCases!!.Bahamas.All, historicDeaths!!.Bahamas.All)
            "Bahrain" -> caseInfo = arrayOf(caseData!!.Bahrain.All, historicCases!!.Bahrain.All, historicDeaths!!.Bahrain.All)
            "Bangladesh" -> caseInfo = arrayOf(caseData!!.Bangladesh.All, historicCases!!.Bangladesh.All, historicDeaths!!.Bangladesh.All)
            "Barbados" -> caseInfo = arrayOf(caseData!!.Barbados.All, historicCases!!.Barbados.All, historicDeaths!!.Barbados.All)
            "Belarus" -> caseInfo = arrayOf(caseData!!.Belarus.All, historicCases!!.Belarus.All, historicDeaths!!.Belarus.All)
            "Belgium" -> caseInfo = arrayOf(caseData!!.Belgium.All, historicCases!!.Belgium.All, historicDeaths!!.Belgium.All)
            "Belize" -> caseInfo = arrayOf(caseData!!.Belize.All, historicCases!!.Belize.All, historicDeaths!!.Belize.All)
            "Benin" -> caseInfo = arrayOf(caseData!!.Benin.All, historicCases!!.Benin.All, historicDeaths!!.Benin.All)
            "Bermuda" -> caseInfo = arrayOf(caseData!!.Bermuda.All, historicCases!!.Bermuda.All, historicDeaths!!.Bermuda.All)
            "Bhutan" -> caseInfo = arrayOf(caseData!!.Bhutan.All, historicCases!!.Bhutan.All, historicDeaths!!.Bhutan.All)
            "Bolivia" -> caseInfo = arrayOf(caseData!!.Bolivia.All, historicCases!!.Bolivia.All, historicDeaths!!.Bolivia.All)
            "Bosnia and Herzegovina" -> caseInfo = arrayOf(caseData!!.BosniaAndHerzegovina.All, historicCases!!.BosniaAndHerzegovina.All, historicDeaths!!.BosniaAndHerzegovina.All)
            "Botswana" -> caseInfo = arrayOf(caseData!!.Botswana.All, historicCases!!.Botswana.All, historicDeaths!!.Botswana.All)
            "Bouvet Island" -> caseInfo = arrayOf(caseData!!.BouvetIsland.All, historicCases!!.BouvetIsland.All, historicDeaths!!.BouvetIsland.All)
            "Brazil" -> caseInfo = arrayOf(caseData!!.Brazil.All, historicCases!!.Brazil.All, historicDeaths!!.Brazil.All)
            "British Indian Ocean Territory" -> caseInfo = arrayOf(caseData!!.BritishIndianOceanTerritory.All, historicCases!!.BritishIndianOceanTerritory.All, historicDeaths!!.BritishIndianOceanTerritory.All)
            "Brunei Darussalam" -> caseInfo = arrayOf(caseData!!.BruneiDarussalam.All, historicCases!!.BruneiDarussalam.All, historicDeaths!!.BruneiDarussalam.All)
            "Bulgaria" -> caseInfo = arrayOf(caseData!!.Bulgaria.All, historicCases!!.Bulgaria.All, historicDeaths!!.Bulgaria.All)
            "Burkina Faso" -> caseInfo = arrayOf(caseData!!.BurkinaFaso.All, historicCases!!.BurkinaFaso.All, historicDeaths!!.BurkinaFaso.All)
            "Burundi" -> caseInfo = arrayOf(caseData!!.Burundi.All, historicCases!!.Burundi.All, historicDeaths!!.Burundi.All)
            "Cambodia" -> caseInfo = arrayOf(caseData!!.Cambodia.All, historicCases!!.Cambodia.All, historicDeaths!!.Cambodia.All)
            "Cameroon" -> caseInfo = arrayOf(caseData!!.Cameroon.All, historicCases!!.Cameroon.All, historicDeaths!!.Cameroon.All)
            "Canada" -> caseInfo = arrayOf(caseData!!.Canada.All, historicCases!!.Canada.All, historicDeaths!!.Canada.All)
            "Cape Verde" -> caseInfo = arrayOf(caseData!!.CapeVerde.All, historicCases!!.CapeVerde.All, historicDeaths!!.CapeVerde.All)
            "Cayman Islands" -> caseInfo = arrayOf(caseData!!.CaymanIslands.All, historicCases!!.CaymanIslands.All, historicDeaths!!.CaymanIslands.All)
            "Central African Republic" -> caseInfo = arrayOf(caseData!!.CentralAfricanRepublic.All, historicCases!!.CentralAfricanRepublic.All, historicDeaths!!.CentralAfricanRepublic.All)
            "Chad" -> caseInfo = arrayOf(caseData!!.Chad.All, historicCases!!.Chad.All, historicDeaths!!.Chad.All)
            "Chile" -> caseInfo = arrayOf(caseData!!.Chile.All, historicCases!!.Chile.All, historicDeaths!!.Chile.All)
            "China" -> caseInfo = arrayOf(caseData!!.China.All, historicCases!!.China.All, historicDeaths!!.China.All)
            "Christmas Island" -> caseInfo = arrayOf(caseData!!.ChristmasIsland.All, historicCases!!.ChristmasIsland.All, historicDeaths!!.ChristmasIsland.All)
            "Cocos" -> caseInfo = arrayOf(caseData!!.Cocos.All, historicCases!!.Cocos.All, historicDeaths!!.Cocos.All)
            "Colombia" -> caseInfo = arrayOf(caseData!!.Colombia.All, historicCases!!.Colombia.All, historicDeaths!!.Colombia.All)
            "Comoros" -> caseInfo = arrayOf(caseData!!.Comoros.All, historicCases!!.Comoros.All, historicDeaths!!.Comoros.All)
            "Congo" -> caseInfo = arrayOf(caseData!!.Congo.All, historicCases!!.Congo.All, historicDeaths!!.Congo.All)
            "Congo DR" -> caseInfo = arrayOf(caseData!!.CongoDR.All, historicCases!!.CongoDR.All, historicDeaths!!.CongoDR.All)
            "Cook Islands" -> caseInfo = arrayOf(caseData!!.CookIslands.All, historicCases!!.CookIslands.All, historicDeaths!!.CookIslands.All)
            "Costa Rica" -> caseInfo = arrayOf(caseData!!.CostaRica.All, historicCases!!.CostaRica.All, historicDeaths!!.CostaRica.All)
            "Cote d'Ivoire" -> caseInfo = arrayOf(caseData!!.CoteDIvoire.All, historicCases!!.CoteDIvoire.All, historicDeaths!!.CoteDIvoire.All)
            "Croatia" -> caseInfo = arrayOf(caseData!!.Croatia.All, historicCases!!.Croatia.All, historicDeaths!!.Croatia.All)
            "Cuba" -> caseInfo = arrayOf(caseData!!.Cuba.All, historicCases!!.Cuba.All, historicDeaths!!.Cuba.All)
            "Cyprus" -> caseInfo = arrayOf(caseData!!.Cyprus.All, historicCases!!.Cyprus.All, historicDeaths!!.Cyprus.All)
            "Czech Republic" -> caseInfo = arrayOf(caseData!!.CzechRepublic.All, historicCases!!.CzechRepublic.All, historicDeaths!!.CzechRepublic.All)
            "Denmark" -> caseInfo = arrayOf(caseData!!.Denmark.All, historicCases!!.Denmark.All, historicDeaths!!.Denmark.All)
            "Djibouti" -> caseInfo = arrayOf(caseData!!.Djibouti.All, historicCases!!.Djibouti.All, historicDeaths!!.Djibouti.All)
            "Dominica" -> caseInfo = arrayOf(caseData!!.Dominica.All, historicCases!!.Dominica.All, historicDeaths!!.Dominica.All)
            "Dominican Republic" -> caseInfo = arrayOf(caseData!!.DominicanRepublic.All, historicCases!!.DominicanRepublic.All, historicDeaths!!.DominicanRepublic.All)
            "East Timor" -> caseInfo = arrayOf(caseData!!.EastTimor.All, historicCases!!.EastTimor.All, historicDeaths!!.EastTimor.All)
            "Ecuador" -> caseInfo = arrayOf(caseData!!.Ecuador.All, historicCases!!.Ecuador.All, historicDeaths!!.Ecuador.All)
            "Egypt" -> caseInfo = arrayOf(caseData!!.Egypt.All, historicCases!!.Egypt.All, historicDeaths!!.Egypt.All)
            "El Salvador" -> caseInfo = arrayOf(caseData!!.ElSalvador.All, historicCases!!.ElSalvador.All, historicDeaths!!.ElSalvador.All)
            "Equatorial Guinea" -> caseInfo = arrayOf(caseData!!.EquatorialGuinea.All, historicCases!!.EquatorialGuinea.All, historicDeaths!!.EquatorialGuinea.All)
            "Eritrea" -> caseInfo = arrayOf(caseData!!.Eritrea.All, historicCases!!.Eritrea.All, historicDeaths!!.Eritrea.All)
            "Estonia" -> caseInfo = arrayOf(caseData!!.Estonia.All, historicCases!!.Estonia.All, historicDeaths!!.Estonia.All)
            "Ethiopia" -> caseInfo = arrayOf(caseData!!.Ethiopia.All, historicCases!!.Ethiopia.All, historicDeaths!!.Ethiopia.All)
            "Falkland Islands" -> caseInfo = arrayOf(caseData!!.FalklandIslands.All, historicCases!!.FalklandIslands.All, historicDeaths!!.FalklandIslands.All)
            "Faroe Islands" -> caseInfo = arrayOf(caseData!!.FaroeIslands.All, historicCases!!.FaroeIslands.All, historicDeaths!!.FaroeIslands.All)
            "Fiji" -> caseInfo = arrayOf(caseData!!.Fiji.All, historicCases!!.Fiji.All, historicDeaths!!.Fiji.All)
            "Finland" -> caseInfo = arrayOf(caseData!!.Finland.All, historicCases!!.Finland.All, historicDeaths!!.Finland.All)
            "France" -> caseInfo = arrayOf(caseData!!.France.All, historicCases!!.France.All, historicDeaths!!.France.All)
            "France Metropolitan" -> caseInfo = arrayOf(caseData!!.FranceMetropolitan.All, historicCases!!.FranceMetropolitan.All, historicDeaths!!.FranceMetropolitan.All)
            "French Guiana" -> caseInfo = arrayOf(caseData!!.FrenchGuiana.All, historicCases!!.FrenchGuiana.All, historicDeaths!!.FrenchGuiana.All)
            "French Polynesia" -> caseInfo = arrayOf(caseData!!.FrenchPolynesia.All, historicCases!!.FrenchPolynesia.All, historicDeaths!!.FrenchPolynesia.All)
            "French Southern Territories" -> caseInfo = arrayOf(caseData!!.FrenchSouthernTerritories.All, historicCases!!.FrenchSouthernTerritories.All, historicDeaths!!.FrenchSouthernTerritories.All)
            "Gabon" -> caseInfo = arrayOf(caseData!!.Gabon.All, historicCases!!.Gabon.All, historicDeaths!!.Gabon.All)
            "Gambia" -> caseInfo = arrayOf(caseData!!.Gambia.All, historicCases!!.Gambia.All, historicDeaths!!.Gambia.All)
            "Georgia" -> caseInfo = arrayOf(caseData!!.Georgia.All, historicCases!!.Georgia.All, historicDeaths!!.Georgia.All)
            "Germany" -> caseInfo = arrayOf(caseData!!.Germany.All, historicCases!!.Germany.All, historicDeaths!!.Germany.All)
            "Ghana" -> caseInfo = arrayOf(caseData!!.Ghana.All, historicCases!!.Ghana.All, historicDeaths!!.Ghana.All)
            "Gibraltar" -> caseInfo = arrayOf(caseData!!.Gibraltar.All, historicCases!!.Gibraltar.All, historicDeaths!!.Gibraltar.All)
            "Greece" -> caseInfo = arrayOf(caseData!!.Greece.All, historicCases!!.Greece.All, historicDeaths!!.Greece.All)
            "Greenland" -> caseInfo = arrayOf(caseData!!.Greenland.All, historicCases!!.Greenland.All, historicDeaths!!.Greenland.All)
            "Grenada" -> caseInfo = arrayOf(caseData!!.Grenada.All, historicCases!!.Grenada.All, historicDeaths!!.Grenada.All)
            "Guadeloupe" -> caseInfo = arrayOf(caseData!!.Guadeloupe.All, historicCases!!.Guadeloupe.All, historicDeaths!!.Guadeloupe.All)
            "Guam" -> caseInfo = arrayOf(caseData!!.Guam.All, historicCases!!.Guam.All, historicDeaths!!.Guam.All)
            "Guatemala" -> caseInfo = arrayOf(caseData!!.Guatemala.All, historicCases!!.Guatemala.All, historicDeaths!!.Guatemala.All)
            "Guinea" -> caseInfo = arrayOf(caseData!!.Guinea.All, historicCases!!.Guinea.All, historicDeaths!!.Guinea.All)
            "Guinea-Bissau" -> caseInfo = arrayOf(caseData!!.GuineaBissau.All, historicCases!!.GuineaBissau.All, historicDeaths!!.GuineaBissau.All)
            "Guyana" -> caseInfo = arrayOf(caseData!!.Guyana.All, historicCases!!.Guyana.All, historicDeaths!!.Guyana.All)
            "Haiti" -> caseInfo = arrayOf(caseData!!.Haiti.All, historicCases!!.Haiti.All, historicDeaths!!.Haiti.All)
            "HeardandMcDonaldIslands" -> caseInfo = arrayOf(caseData!!.HeardAndMcDonaldIslands.All, historicCases!!.HeardAndMcDonaldIslands.All, historicDeaths!!.HeardAndMcDonaldIslands.All)
            "HolySee" -> caseInfo = arrayOf(caseData!!.HolySee.All, historicCases!!.HolySee.All, historicDeaths!!.HolySee.All)
            "Honduras" -> caseInfo = arrayOf(caseData!!.Honduras.All, historicCases!!.Honduras.All, historicDeaths!!.Honduras.All)
            "HongKong" -> caseInfo = arrayOf(caseData!!.HongKong.All, historicCases!!.HongKong.All, historicDeaths!!.HongKong.All)
            "Hungary" -> caseInfo = arrayOf(caseData!!.Hungary.All, historicCases!!.Hungary.All, historicDeaths!!.Hungary.All)
            "Iceland" -> caseInfo = arrayOf(caseData!!.Iceland.All, historicCases!!.Iceland.All, historicDeaths!!.Iceland.All)
            "India" -> caseInfo = arrayOf(caseData!!.India.All, historicCases!!.India.All, historicDeaths!!.India.All)
            "Indonesia" -> caseInfo = arrayOf(caseData!!.Indonesia.All, historicCases!!.Indonesia.All, historicDeaths!!.Indonesia.All)
            "Iran" -> caseInfo = arrayOf(caseData!!.Iran.All, historicCases!!.Iran.All, historicDeaths!!.Iran.All)
            "Iraq" -> caseInfo = arrayOf(caseData!!.Iraq.All, historicCases!!.Iraq.All, historicDeaths!!.Iraq.All)
            "Ireland" -> caseInfo = arrayOf(caseData!!.Ireland.All, historicCases!!.Ireland.All, historicDeaths!!.Ireland.All)
            "Israel" -> caseInfo = arrayOf(caseData!!.Israel.All, historicCases!!.Israel.All, historicDeaths!!.Israel.All)
            "Italy" -> caseInfo = arrayOf(caseData!!.Italy.All, historicCases!!.Italy.All, historicDeaths!!.Italy.All)
            "Jamaica" -> caseInfo = arrayOf(caseData!!.Jamaica.All, historicCases!!.Jamaica.All, historicDeaths!!.Jamaica.All)
            "Japan" -> caseInfo = arrayOf(caseData!!.Japan.All, historicCases!!.Japan.All, historicDeaths!!.Japan.All)
            "Jordan" -> caseInfo = arrayOf(caseData!!.Jordan.All, historicCases!!.Jordan.All, historicDeaths!!.Jordan.All)
            "Kazakhstan" -> caseInfo = arrayOf(caseData!!.Kazakhstan.All, historicCases!!.Kazakhstan.All, historicDeaths!!.Kazakhstan.All)
            "Kenya" -> caseInfo = arrayOf(caseData!!.Kenya.All, historicCases!!.Kenya.All, historicDeaths!!.Kenya.All)
            "Kiribati" -> caseInfo = arrayOf(caseData!!.Kiribati.All, historicCases!!.Kiribati.All, historicDeaths!!.Kiribati.All)
            "North Korea" -> caseInfo = arrayOf(caseData!!.NorthKorea.All, historicCases!!.NorthKorea.All, historicDeaths!!.NorthKorea.All)
            "South Korea" -> caseInfo = arrayOf(caseData!!.SouthKorea.All, historicCases!!.SouthKorea.All, historicDeaths!!.SouthKorea.All)
            "Kuwait" -> caseInfo = arrayOf(caseData!!.Kuwait.All, historicCases!!.Kuwait.All, historicDeaths!!.Kuwait.All)
            "Kyrgyzstan" -> caseInfo = arrayOf(caseData!!.Kyrgyzstan.All, historicCases!!.Kyrgyzstan.All, historicDeaths!!.Kyrgyzstan.All)
            "Lao,People'sDemocraticRepublic" -> caseInfo = arrayOf(caseData!!.Lao.All, historicDeaths!!.Lao.All, historicDeaths!!.Lao.All)
            "Latvia" -> caseInfo = arrayOf(caseData!!.Latvia.All, historicCases!!.Latvia.All, historicDeaths!!.Latvia.All)
            "Lebanon" -> caseInfo = arrayOf(caseData!!.Lebanon.All, historicCases!!.Lebanon.All, historicDeaths!!.Lebanon.All)
            "Lesotho" -> caseInfo = arrayOf(caseData!!.Lesotho.All, historicCases!!.Lesotho.All, historicDeaths!!.Lesotho.All)
            "Liberia" -> caseInfo = arrayOf(caseData!!.Liberia.All, historicCases!!.Liberia.All, historicDeaths!!.Liberia.All)
            "LibyanArabJamahiriya" -> caseInfo = arrayOf(caseData!!.LibyanArabJamahiriya.All, historicCases!!.LibyanArabJamahiriya.All, historicDeaths!!.LibyanArabJamahiriya.All)
            "Liechtenstein" -> caseInfo = arrayOf(caseData!!.Liechtenstein.All, historicCases!!.Liechtenstein.All, historicDeaths!!.Liechtenstein.All)
            "Lithuania" -> caseInfo = arrayOf(caseData!!.Lithuania.All, historicCases!!.Lithuania.All, historicDeaths!!.Lithuania.All)
            "Luxembourg" -> caseInfo = arrayOf(caseData!!.Luxembourg.All, historicCases!!.Luxembourg.All, historicDeaths!!.Luxembourg.All)
            "Macau" -> caseInfo = arrayOf(caseData!!.Macau.All, historicCases!!.Macau.All, historicDeaths!!.Macau.All)
            "Macedonia" -> caseInfo = arrayOf(caseData!!.Macedonia.All, historicCases!!.Macedonia.All, historicDeaths!!.Macedonia.All)
            "Madagascar" -> caseInfo = arrayOf(caseData!!.Madagascar.All, historicCases!!.Madagascar.All, historicDeaths!!.Madagascar.All)
            "Malawi" -> caseInfo = arrayOf(caseData!!.Malawi.All, historicCases!!.Malawi.All, historicDeaths!!.Malawi.All)
            "Malaysia" -> caseInfo = arrayOf(caseData!!.Malaysia.All, historicCases!!.Malaysia.All, historicDeaths!!.Malaysia.All)
            "Maldives" -> caseInfo = arrayOf(caseData!!.Maldives.All, historicCases!!.Maldives.All, historicDeaths!!.Maldives.All)
            "Mali" -> caseInfo = arrayOf(caseData!!.Mali.All, historicCases!!.Mali.All, historicDeaths!!.Mali.All)
            "Malta" -> caseInfo = arrayOf(caseData!!.Malta.All, historicCases!!.Malta.All, historicDeaths!!.Malta.All)
            "MarshallIslands" -> caseInfo = arrayOf(caseData!!.MarshallIslands.All, historicCases!!.MarshallIslands.All, historicDeaths!!.MarshallIslands.All)
            "Martinique" -> caseInfo = arrayOf(caseData!!.Martinique.All, historicCases!!.Martinique.All, historicDeaths!!.Martinique.All)
            "Mauritania" -> caseInfo = arrayOf(caseData!!.Mauritania.All, historicCases!!.Mauritania.All, historicDeaths!!.Mauritania.All)
            "Mauritius" -> caseInfo = arrayOf(caseData!!.Mauritius.All, historicCases!!.Mauritius.All, historicDeaths!!.Mauritius.All)
            "Mayotte" -> caseInfo = arrayOf(caseData!!.Mayotte.All, historicCases!!.Mayotte.All, historicDeaths!!.Mayotte.All)
            "Mexico" -> caseInfo = arrayOf(caseData!!.Mexico.All, historicCases!!.Mexico.All, historicDeaths!!.Mexico.All)
            "Micronesia,FederatedStatesof" -> caseInfo = arrayOf(caseData!!.Micronesia.All, historicCases!!.Micronesia.All, historicDeaths!!.Micronesia.All)
            "Moldova,Republicof" -> caseInfo = arrayOf(caseData!!.Moldova.All, historicCases!!.Moldova.All, historicDeaths!!.Moldova.All)
            "Monaco" -> caseInfo = arrayOf(caseData!!.Monaco.All, historicCases!!.Monaco.All, historicDeaths!!.Monaco.All)
            "Mongolia" -> caseInfo = arrayOf(caseData!!.Mongolia.All, historicCases!!.Mongolia.All, historicDeaths!!.Mongolia.All)
            "Montserrat" -> caseInfo = arrayOf(caseData!!.Montserrat.All, historicCases!!.Montserrat.All, historicDeaths!!.Montserrat.All)
            "Morocco" -> caseInfo = arrayOf(caseData!!.Morocco.All, historicCases!!.Morocco.All, historicDeaths!!.Morocco.All)
            "Mozambique" -> caseInfo = arrayOf(caseData!!.Mozambique.All, historicCases!!.Mozambique.All, historicDeaths!!.Mozambique.All)
            "Myanmar" -> caseInfo = arrayOf(caseData!!.Myanmar.All, historicCases!!.Myanmar.All, historicDeaths!!.Myanmar.All)
            "Namibia" -> caseInfo = arrayOf(caseData!!.Namibia.All, historicCases!!.Namibia.All, historicDeaths!!.Namibia.All)
            "Nauru" -> caseInfo = arrayOf(caseData!!.Nauru.All, historicCases!!.Nauru.All, historicDeaths!!.Nauru.All)
            "Nepal" -> caseInfo = arrayOf(caseData!!.Nepal.All, historicCases!!.Nepal.All, historicDeaths!!.Nepal.All)
            "Netherlands" -> caseInfo = arrayOf(caseData!!.Netherlands.All, historicCases!!.Netherlands.All, historicDeaths!!.Netherlands.All)
            "NetherlandsAntilles" -> caseInfo = arrayOf(caseData!!.NetherlandsAntilles.All, historicCases!!.NetherlandsAntilles.All, historicDeaths!!.NetherlandsAntilles.All)
            "NewCaledonia" -> caseInfo = arrayOf(caseData!!.NewCaledonia.All, historicCases!!.NewCaledonia.All, historicDeaths!!.NewCaledonia.All)
            "NewZealand" -> caseInfo = arrayOf(caseData!!.NewZealand.All, historicCases!!.NewZealand.All, historicDeaths!!.NewZealand.All)
            "Nicaragua" -> caseInfo = arrayOf(caseData!!.Nicaragua.All, historicCases!!.Nicaragua.All, historicDeaths!!.Nicaragua.All)
            "Niger" -> caseInfo = arrayOf(caseData!!.Niger.All, historicCases!!.Niger.All, historicDeaths!!.Niger.All)
            "Nigeria" -> caseInfo = arrayOf(caseData!!.Nigeria.All, historicCases!!.Nigeria.All, historicDeaths!!.Nigeria.All)
            "Niue" -> caseInfo = arrayOf(caseData!!.Niue.All, historicCases!!.Niue.All, historicDeaths!!.Niue.All)
            "NorfolkIsland" -> caseInfo = arrayOf(caseData!!.NorfolkIsland.All, historicCases!!.NorfolkIsland.All, historicDeaths!!.NorfolkIsland.All)
            "NorthernMarianaIslands" -> caseInfo = arrayOf(caseData!!.NorthernMarianaIslands.All, historicCases!!.NorthernMarianaIslands.All, historicDeaths!!.NorthernMarianaIslands.All)
            "Norway" -> caseInfo = arrayOf(caseData!!.Norway.All, historicCases!!.Norway.All, historicDeaths!!.Norway.All)
            "Oman" -> caseInfo = arrayOf(caseData!!.Oman.All, historicCases!!.Oman.All, historicDeaths!!.Oman.All)
            "Pakistan" -> caseInfo = arrayOf(caseData!!.Pakistan.All, historicCases!!.Pakistan.All, historicDeaths!!.Pakistan.All)
            "Palau" -> caseInfo = arrayOf(caseData!!.Palau.All, historicCases!!.Palau.All, historicDeaths!!.Palau.All)
            "Panama" -> caseInfo = arrayOf(caseData!!.Panama.All, historicCases!!.Panama.All, historicDeaths!!.Panama.All)
            "PapuaNewGuinea" -> caseInfo = arrayOf(caseData!!.PapuaNewGuinea.All, historicCases!!.PapuaNewGuinea.All, historicDeaths!!.PapuaNewGuinea.All)
            "Paraguay" -> caseInfo = arrayOf(caseData!!.Paraguay.All, historicCases!!.Paraguay.All, historicDeaths!!.Paraguay.All)
            "Peru" -> caseInfo = arrayOf(caseData!!.Peru.All, historicCases!!.Peru.All, historicDeaths!!.Peru.All)
            "Philippines" -> caseInfo = arrayOf(caseData!!.Philippines.All, historicCases!!.Philippines.All, historicDeaths!!.Philippines.All)
            "Pitcairn" -> caseInfo = arrayOf(caseData!!.Pitcairn.All, historicCases!!.Pitcairn.All, historicDeaths!!.Pitcairn.All)
            "Poland" -> caseInfo = arrayOf(caseData!!.Poland.All, historicCases!!.Poland.All, historicDeaths!!.Poland.All)
            "Portugal" -> caseInfo = arrayOf(caseData!!.Portugal.All, historicCases!!.Portugal.All, historicDeaths!!.Portugal.All)
            "PuertoRico" -> caseInfo = arrayOf(caseData!!.PuertoRico.All, historicCases!!.PuertoRico.All, historicDeaths!!.PuertoRico.All)
            "Qatar" -> caseInfo = arrayOf(caseData!!.Qatar.All, historicCases!!.Qatar.All, historicDeaths!!.Qatar.All)
            "Reunion" -> caseInfo = arrayOf(caseData!!.Reunion.All, historicCases!!.Reunion.All, historicDeaths!!.Reunion.All)
            "Romania" -> caseInfo = arrayOf(caseData!!.Romania.All, historicCases!!.Romania.All, historicDeaths!!.Romania.All)
            "RussianFederation" -> caseInfo = arrayOf(caseData!!.RussianFederation.All, historicCases!!.RussianFederation.All, historicDeaths!!.RussianFederation.All)
            "Rwanda" -> caseInfo = arrayOf(caseData!!.Rwanda.All, historicCases!!.Rwanda.All, historicDeaths!!.Rwanda.All)
            "SaintKittsandNevis" -> caseInfo = arrayOf(caseData!!.SaintKittsAndNevis.All, historicCases!!.SaintKittsAndNevis.All, historicDeaths!!.SaintKittsAndNevis.All)
            "SaintLucia" -> caseInfo = arrayOf(caseData!!.SaintLucia.All, historicCases!!.SaintLucia.All, historicDeaths!!.SaintLucia.All)
            "SaintVincentandtheGrenadines" -> caseInfo = arrayOf(caseData!!.SaintVincentAndTheGrenadines.All, historicCases!!.SaintVincentAndTheGrenadines.All, historicDeaths!!.SaintVincentAndTheGrenadines.All)
            "Samoa" -> caseInfo = arrayOf(caseData!!.Samoa.All, historicCases!!.Samoa.All, historicDeaths!!.Samoa.All)
            "SanMarino" -> caseInfo = arrayOf(caseData!!.SanMarino.All, historicCases!!.SanMarino.All, historicDeaths!!.SanMarino.All)
            "SaoTomeandPrincipe" -> caseInfo = arrayOf(caseData!!.SaoTomeAndPrincipe.All, historicCases!!.SaoTomeAndPrincipe.All, historicDeaths!!.SaoTomeAndPrincipe.All)
            "SaudiArabia" -> caseInfo = arrayOf(caseData!!.SaudiArabia.All, historicCases!!.SaudiArabia.All, historicDeaths!!.SaudiArabia.All)
            "Senegal" -> caseInfo = arrayOf(caseData!!.Senegal.All, historicCases!!.Senegal.All, historicDeaths!!.Senegal.All)
            "Seychelles" -> caseInfo = arrayOf(caseData!!.Seychelles.All, historicCases!!.Seychelles.All, historicDeaths!!.Seychelles.All)
            "SierraLeone" -> caseInfo = arrayOf(caseData!!.SierraLeone.All, historicCases!!.SierraLeone.All, historicDeaths!!.SierraLeone.All)
            "Singapore" -> caseInfo = arrayOf(caseData!!.Singapore.All, historicCases!!.Singapore.All, historicDeaths!!.Singapore.All)
            "Slovakia" -> caseInfo = arrayOf(caseData!!.Slovakia.All, historicCases!!.Slovakia.All, historicDeaths!!.Slovakia.All)
            "Slovenia" -> caseInfo = arrayOf(caseData!!.Slovenia.All, historicCases!!.Slovenia.All, historicDeaths!!.Slovenia.All)
            "SolomonIslands" -> caseInfo = arrayOf(caseData!!.SolomonIslands.All, historicCases!!.SolomonIslands.All, historicDeaths!!.SolomonIslands.All)
            "Somalia" -> caseInfo = arrayOf(caseData!!.Somalia.All, historicCases!!.Somalia.All, historicDeaths!!.Somalia.All)
            "SouthAfrica" -> caseInfo = arrayOf(caseData!!.SouthAfrica.All, historicCases!!.SouthAfrica.All, historicDeaths!!.SouthAfrica.All)
            "SouthGeorgiaandtheSouthSandwichIslands" -> caseInfo = arrayOf(caseData!!.SouthGeorgiaAndTheSouthSandwichIslands.All, historicCases!!.SouthGeorgiaAndTheSouthSandwichIslands.All, historicDeaths!!.SouthGeorgiaAndTheSouthSandwichIslands.All)
            "Spain" -> caseInfo = arrayOf(caseData!!.Spain.All, historicCases!!.Spain.All, historicDeaths!!.Spain.All)
            "SriLanka" -> caseInfo = arrayOf(caseData!!.SriLanka.All, historicCases!!.SriLanka.All, historicDeaths!!.SriLanka.All)
            "St.Helena" -> caseInfo = arrayOf(caseData!!.StHelena.All, historicCases!!.StHelena.All, historicDeaths!!.StHelena.All)
            "St.PierreandMiquelon" -> caseInfo = arrayOf(caseData!!.StPierreAndMiquelon.All, historicCases!!.StPierreAndMiquelon.All, historicDeaths!!.StPierreAndMiquelon.All)
            "Sudan" -> caseInfo = arrayOf(caseData!!.Sudan.All, historicCases!!.Sudan.All, historicDeaths!!.Sudan.All)
            "Suriname" -> caseInfo = arrayOf(caseData!!.Suriname.All, historicCases!!.Suriname.All, historicDeaths!!.Suriname.All)
            "SvalbardandJanMayenIslands" -> caseInfo = arrayOf(caseData!!.SvalbardAndJanMayenIslands.All, historicCases!!.SvalbardAndJanMayenIslands.All, historicDeaths!!.SvalbardAndJanMayenIslands.All)
            "Swaziland" -> caseInfo = arrayOf(caseData!!.Swaziland.All, historicCases!!.Swaziland.All, historicDeaths!!.Swaziland.All)
            "Sweden" -> caseInfo = arrayOf(caseData!!.Sweden.All, historicCases!!.Sweden.All, historicDeaths!!.Sweden.All)
            "Switzerland" -> caseInfo = arrayOf(caseData!!.Switzerland.All, historicCases!!.Switzerland.All, historicDeaths!!.Switzerland.All)
            "SyrianArabRepublic" -> caseInfo = arrayOf(caseData!!.SyrianArabRepublic.All, historicCases!!.SyrianArabRepublic.All, historicDeaths!!.SyrianArabRepublic.All)
            "Taiwan,ProvinceofChina" -> caseInfo = arrayOf(caseData!!.Taiwan.All, historicCases!!.Taiwan.All, historicDeaths!!.Taiwan.All)
            "Tajikistan" -> caseInfo = arrayOf(caseData!!.Tajikistan.All, historicCases!!.Tajikistan.All, historicDeaths!!.Tajikistan.All)
            "Tanzania,UnitedRepublicof" -> caseInfo = arrayOf(caseData!!.Tanzania.All, historicCases!!.Tanzania.All, historicDeaths!!.Tanzania.All)
            "Thailand" -> caseInfo = arrayOf(caseData!!.Thailand.All, historicCases!!.Thailand.All, historicDeaths!!.Thailand.All)
            "Togo" -> caseInfo = arrayOf(caseData!!.Togo.All, historicCases!!.Togo.All, historicDeaths!!.Togo.All)
            "Tokelau" -> caseInfo = arrayOf(caseData!!.Tokelau.All, historicCases!!.Tokelau.All, historicDeaths!!.Tokelau.All)
            "Tonga" -> caseInfo = arrayOf(caseData!!.Tonga.All, historicCases!!.Tonga.All, historicDeaths!!.Tonga.All)
            "TrinidadandTobago" -> caseInfo = arrayOf(caseData!!.TrinidadAndTobago.All, historicCases!!.TrinidadAndTobago.All, historicDeaths!!.TrinidadAndTobago.All)
            "Tunisia" -> caseInfo = arrayOf(caseData!!.Tunisia.All, historicCases!!.Tunisia.All, historicDeaths!!.Tunisia.All)
            "Turkey" -> caseInfo = arrayOf(caseData!!.Turkey.All, historicCases!!.Turkey.All, historicDeaths!!.Turkey.All)
            "Turkmenistan" -> caseInfo = arrayOf(caseData!!.Turkmenistan.All, historicCases!!.Turkmenistan.All, historicDeaths!!.Turkmenistan.All)
            "TurksandCaicosIslands" -> caseInfo = arrayOf(caseData!!.TurksAndCaicosIslands.All, historicCases!!.TurksAndCaicosIslands.All, historicDeaths!!.TurksAndCaicosIslands.All)
            "Tuvalu" -> caseInfo = arrayOf(caseData!!.Tuvalu.All, historicCases!!.Tuvalu.All, historicDeaths!!.Tuvalu.All)
            "Uganda" -> caseInfo = arrayOf(caseData!!.Uganda.All, historicCases!!.Uganda.All, historicDeaths!!.Uganda.All)
            "Ukraine" -> caseInfo = arrayOf(caseData!!.Ukraine.All, historicCases!!.Ukraine.All, historicDeaths!!.Ukraine.All)
            "UnitedArabEmirates" -> caseInfo = arrayOf(caseData!!.UnitedArabEmirates.All, historicCases!!.UnitedArabEmirates.All, historicDeaths!!.UnitedArabEmirates.All)
            "UnitedKingdom" -> caseInfo = arrayOf(caseData!!.UnitedKingdom.All, historicCases!!.UnitedKingdom.All, historicDeaths!!.UnitedKingdom.All)
            "UnitedStates" -> caseInfo = arrayOf(caseData!!.UnitedStates.All, historicCases!!.UnitedStates.All, historicDeaths!!.UnitedStates.All)
            "UnitedStatesMinorOutlyingIslands" -> caseInfo = arrayOf(caseData!!.UnitedStatesMinorOutlyingIslands.All, historicCases!!.UnitedStatesMinorOutlyingIslands.All, historicDeaths!!.UnitedStatesMinorOutlyingIslands.All)
            "Uruguay" -> caseInfo = arrayOf(caseData!!.Uruguay.All, historicCases!!.Uruguay.All, historicDeaths!!.Uruguay.All)
            "Uzbekistan" -> caseInfo = arrayOf(caseData!!.Uzbekistan.All, historicCases!!.Uzbekistan.All, historicDeaths!!.Uzbekistan.All)
            "Vanuatu" -> caseInfo = arrayOf(caseData!!.Vanuatu.All, historicCases!!.Vanuatu.All, historicDeaths!!.Vanuatu.All)
            "Venezuela" -> caseInfo = arrayOf(caseData!!.Venezuela.All, historicCases!!.Venezuela.All, historicDeaths!!.Venezuela.All)
            "Vietnam" -> caseInfo = arrayOf(caseData!!.Vietnam.All, historicCases!!.Vietnam.All, historicDeaths!!.Vietnam.All)
            "VirginIslands" -> caseInfo = arrayOf(caseData!!.VirginIslandsUK.All, historicCases!!.VirginIslandsUK.All, historicDeaths!!.VirginIslandsUK.All)
            "VirginIslands" -> caseInfo = arrayOf(caseData!!.VirginIslandsUS.All, historicCases!!.VirginIslandsUS.All, historicDeaths!!.VirginIslandsUS.All)
            "Wallis and Futuna Islands" -> caseInfo = arrayOf(caseData!!.WallisAndFutunaIslands.All, historicCases!!.WallisAndFutunaIslands.All, historicDeaths!!.WallisAndFutunaIslands.All)
            "WesternSahara" -> caseInfo = arrayOf(caseData!!.WesternSahara.All, historicCases!!.WesternSahara.All, historicDeaths!!.WesternSahara.All)
            "Yemen" -> caseInfo = arrayOf(caseData!!.Yemen.All, historicCases!!.Yemen.All, historicDeaths!!.Yemen.All)
            "Yugoslavia" -> caseInfo = arrayOf(caseData!!.Yugoslavia.All, historicCases!!.Yugoslavia.All, historicDeaths!!.Yugoslavia.All)
            "Zambia" -> caseInfo = arrayOf(caseData!!.Zambia.All, historicCases!!.Zambia.All, historicDeaths!!.Zambia.All)
            "Zimbabwe" -> caseInfo = arrayOf(caseData!!.Zimbabwe.All, historicCases!!.Zimbabwe.All, historicDeaths!!.Zimbabwe.All)
            "Palestine" -> caseInfo = arrayOf(caseData!!.Palestine.All, historicCases!!.Palestine.All, historicDeaths!!.Palestine.All)



        }
        return caseInfo
    }

    //onStop newly fetched Data has to be inserted into database
    override fun onStop() {
        Log.i("DATA SAVED" , "YES")
        super.onStop()
    }
}