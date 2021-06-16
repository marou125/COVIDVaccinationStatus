package com.marou125.covidvaccinationstatus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.marou125.covidvaccinationstatus.database.CountryRepository
import com.marou125.covidvaccinationstatus.databinding.ActivityCountryDetailBinding
import com.marou125.covidvaccinationstatus.databinding.AlertDialogBinding
import com.marou125.covidvaccinationstatus.service.CaseInfo
import com.marou125.covidvaccinationstatus.service.VaccinationData
import java.lang.Exception
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.reflect.KFunction

//TODO: Refactoring, flag appears small on some countries, create viewmodel for this class
class CountryDetailActivity : AppCompatActivity() {

    val countryRepository by lazy {
        CountryRepository.get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityCountryDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_country_detail)

        setSupportActionBar(findViewById(R.id.toolbar_detail))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val countryID = intent.getIntExtra("country", 0)
        if (countryID != 0) {
            Log.i("The country is ", getString(countryID))
            for (c in CountryDataSingleton.world) {
                if (c.name == countryID) {
                    Log.i("Is it in the database", c.date)
                    break;
                }
            }
        }

        try {

            //Find data for country in Singleton List
            val displayedCountryData: VaccinationData? = findCountryData(countryID)

            if(displayedCountryData != null){
                Log.i(
                    "Last vaccination update",
                    displayedCountryData.data[displayedCountryData.data.size - 1].date
                )
            }


            //Find data for country in Json Case Data object
            val caseInfoArray = findCaseData(countryID)
            val displayedCountryCaseData = caseInfoArray[0]

            //Get last recorded seven days of cases and deaths saved into an Array
            var threeWeekCases = IntArray(21)
            var threeWeekDeaths = IntArray(21)
            var newCases = 0
            var newDeaths = 0
            if(caseInfoArray[1] != null){
                threeWeekCases = CountryDataSingleton.getWeeklyData(caseInfoArray[1]!!.dates)
                newCases = threeWeekCases[0] - threeWeekCases[1]

            }
            if(caseInfoArray[2] != null){
                threeWeekDeaths = CountryDataSingleton.getWeeklyData(caseInfoArray[2]!!.dates)
                newDeaths = threeWeekDeaths[0] - threeWeekDeaths[1]

            }

            Log.i("LAST WEEK CASES in $countryID", Arrays.toString(threeWeekCases))
            Log.i("LAST WEEK DEATHS in $countryID", Arrays.toString(threeWeekDeaths))

            //Values were fetched and are
            if (displayedCountryData != null) {
                Log.i("Fetched data for", displayedCountryData.country)
            }


            //Fetch Country data from database
            val displayedCountry = countryRepository.getCountry(countryID)


            //Set data according to clicked country
            displayedCountry.observe(
                this,
                Observer { country ->

                    var totalVacIncrease = 0
                    var firstVacIncrease = 0
                    var secondVacIncrease = 0
                    var averagePerDayChange = 0

                    var totalVaccinationsUpdated = true
                    var firstVaccinationsUpdated = true
                    var fullyVaccinatedUpdated = true
                    var sevenDayAverageUpdated = true

                    //Update country data to new values fetched from web
                    if (displayedCountryData != null) {
                        val arrayLength = displayedCountryData.data.size
                        val lastUpdate = displayedCountryData.data[arrayLength - 1]

                        country!!.date = formatDate(lastUpdate.date)
                        if (lastUpdate.total_vaccinations != null) {
                            country.totalVaccinations =
                                Integer.valueOf(lastUpdate.total_vaccinations)
                        } else {
                            totalVaccinationsUpdated = false
                        }
                        if (lastUpdate.people_vaccinated != null) {
                            country.firstVaccine = Integer.valueOf(lastUpdate.people_vaccinated)
                        } else {
                            firstVaccinationsUpdated = false
                        }
                        if (lastUpdate.people_fully_vaccinated != null) {
                            country.fullyVaccinated =
                                Integer.valueOf(lastUpdate.people_fully_vaccinated)
                        } else {
                            fullyVaccinatedUpdated = false
                        }
                        if (lastUpdate.daily_vaccinations != null) {
                            country.sevenDayAverage = Integer.valueOf(lastUpdate.daily_vaccinations)
                        } else {
                            sevenDayAverageUpdated = false
                        }

                        if (displayedCountryData.data.size > 1) {
                            val updateBefore = displayedCountryData.data[arrayLength - 2]

                            val totalVaccinationsOld: Int? =
                                if (updateBefore.total_vaccinations != null) Integer.valueOf(
                                    updateBefore.total_vaccinations
                                ) else null
                            val firstVaccinationsOld: Int? =
                                if (updateBefore.people_vaccinated != null) Integer.valueOf(
                                    updateBefore.people_vaccinated
                                ) else null
                            val secondVaccinationsOld: Int? =
                                if (updateBefore.people_fully_vaccinated != null) Integer.valueOf(
                                    updateBefore.people_fully_vaccinated
                                ) else null
                            val averageVaccinationsOld: Int? =
                                if (updateBefore.daily_vaccinations != null) Integer.valueOf(
                                    updateBefore.daily_vaccinations
                                ) else null

                            totalVacIncrease =
                                if (totalVaccinationsOld != null) country.totalVaccinations - totalVaccinationsOld else 0
                            firstVacIncrease =
                                if (firstVaccinationsOld != null) country.firstVaccine - firstVaccinationsOld else 0
                            secondVacIncrease =
                                if (secondVaccinationsOld != null) country.fullyVaccinated - secondVaccinationsOld else 0
                            averagePerDayChange =
                                if (averageVaccinationsOld != null) country.sevenDayAverage - averageVaccinationsOld else 0

                            //If the number is the same as from the update before then it was not updated and might be out of date
                            if(country.totalVaccinations == totalVaccinationsOld) totalVaccinationsUpdated = false
                            if(country.firstVaccine == firstVaccinationsOld) firstVaccinationsUpdated = false
                            if(country.fullyVaccinated == secondVaccinationsOld) fullyVaccinatedUpdated = false
                            if(country.sevenDayAverage == averageVaccinationsOld) sevenDayAverageUpdated = false
                        }


                    }

                    if (displayedCountryCaseData != null && displayedCountryCaseData.confirmed != country!!.totalCases) {
                        country!!.newDeaths = newDeaths
                        country.totalDeaths = displayedCountryCaseData.deaths
                        country.newCases = newCases
                        country.totalCases = displayedCountryCaseData.confirmed
                        //The active cases are the confirmed cases subtracted by the recovered people and people who passed away
                        val activeCasesYesterday = threeWeekCases[1] - (threeWeekCases[19] - threeWeekDeaths[19]) - threeWeekDeaths[1]
                        country.activeCases =
                            displayedCountryCaseData.confirmed - (threeWeekCases[18]-threeWeekDeaths[18]) - country.totalDeaths
                        country.activeCasesChange = country.activeCases - activeCasesYesterday

                    }

                    val detailFlagIV = findViewById<ImageView>(R.id.detail_flag_iv)
                    val detailCountryNameTV = findViewById<TextView>(R.id.detail_country_name_tv)
                    detailFlagIV.setImageResource(country!!.flag)
                    detailCountryNameTV.text = getString(country.name)


                    //Infection Status CardView
                    binding.populationNumberTv.text = formatNumber(country.population)
                    binding.caseNumberTv.let {
                        if(country.totalCases != 0){
                            it.text = formatNumber(country.totalCases)
                        }
                    }
                    binding.newCasesTv.let {
                        if(country.newCases != 0){
                            it.visibility = TextView.VISIBLE
                            it.text = "(+${formatNumber(country.newCases)})"
                        }
                    }
                    binding.activeCasesNumberTv.let {
                        if(country.activeCases == 0){
                            //If the active cases are 0 but the total case number has data then the active cases have to be displayed as 0, otherwise leave them as no data
                            if(!binding.caseNumberTv.text.equals(getString(R.string.no_data))){
                                it.text = "0"
                            }
                        } else {
                            it.text = formatNumber(country.activeCases)
                        }

                    }
                    binding.totalDeathsNumberTv.let {
                        if(country.totalDeaths != 0){
                            it.text = formatNumber(country.totalDeaths)
                        }
                    }
                    binding.newDeathsNumberTv.let {
                        if(country.newDeaths != 0){
                            it.visibility = TextView.VISIBLE
                            it.text = "(+${formatNumber(country.newDeaths)})"
                        }
                    }


                    //Vaccinations Card View
                    binding.dateDataTv.text =
                        if(country.date == "2021-01-01") getString(R.string.no_data) else country.date
                    binding.totalVaccNumberTv.text =
                        if (country.totalVaccinations == 0) getString(R.string.no_data) else formatNumber(country.totalVaccinations)
                    binding.peopleVaccNumberTv.text =
                        if (country.firstVaccine == 0) getString(R.string.no_data) else formatNumber(country.firstVaccine)
                    binding.peopleFullVaccNumberTv.text =
                        if (country.fullyVaccinated == 0) getString(R.string.no_data) else formatNumber(country.fullyVaccinated)
                    binding.sevenDayAvgNumber.text =
                        if (country.sevenDayAverage == 0) getString(R.string.no_data) else formatNumber(country.sevenDayAverage)

                    binding.totalVacIncrease.let {
                        if (totalVacIncrease != 0 /*&& totalVaccinationsOld != country.totalVaccinations*/) {
                            it.text = "(+${formatNumber(totalVacIncrease)})"
                        }
                    }

                    binding.firstVacIncrease.let {
                        if (firstVacIncrease != 0 /*&& firstVaccinationsOld != country.firstVaccine*/) {
                            it.text = "(+${formatNumber(firstVacIncrease)})"
                        }
                    }

                    binding.fullVacIncrease.let {
                        if (secondVacIncrease != 0 /*&& secondVaccinationsOld != country.fullyVaccinated*/) {
                            it.text = "(+${formatNumber(secondVacIncrease)})"
                        }
                    }
                    binding.averageVacChange.let {
                        if (averagePerDayChange != 0 /*&& averageVaccinationsOld != country.sevenDayAverage*/) {
                            if (averagePerDayChange > 0) {
                                it.text = "(+${formatNumber(averagePerDayChange)})"
                            } else {
                                it.text = "(${formatNumber(averagePerDayChange)})"
                            }
                        }
                    }

                    //Warning flags
                    binding.totalVacNotUpdatedFlag.let {
                        if(binding.totalVaccNumberTv.text == getString(R.string.no_data)){
                            totalVaccinationsUpdated = true
                        }
                        if (totalVaccinationsUpdated == false) {
                            it.visibility = ImageView.VISIBLE
                        }
                    }
                    binding.firstVaccineNotUpdatedFlag.let {
                        if (binding.peopleVaccNumberTv.text == getString(R.string.no_data)) {
                            firstVaccinationsUpdated = true
                        }
                        if (firstVaccinationsUpdated == false) {
                            it.visibility = ImageView.VISIBLE
                        }
                    }
                    binding.fullVaccineNotUpdatedFlag.let {
                        if (binding.peopleFullVaccNumberTv.text == getString(R.string.no_data)) {
                            fullyVaccinatedUpdated = true
                        }
                        if (fullyVaccinatedUpdated == false) {
                            it.visibility = ImageView.VISIBLE
                        }
                    }
                    binding.sevenDayAvgNotUpdatedFlag.let {
                        if(binding.sevenDayAvgNumber.text == getString(R.string.no_data)){
                            sevenDayAverageUpdated = true
                        }
                        if (sevenDayAverageUpdated == false) {
                            it.visibility = ImageView.VISIBLE
                        }
                    }

                    if (!(totalVaccinationsUpdated && firstVaccinationsUpdated && fullyVaccinatedUpdated && sevenDayAverageUpdated)) {
                        binding.explanationFlag.visibility = ImageView.VISIBLE
                        binding.explanationTv.visibility = TextView.VISIBLE
                    }


                    //ProgressBar
                    if (binding.peopleFullVaccNumberTv.text.equals(getString(R.string.no_data))) {
                        binding.progressBarFully.visibility = View.GONE
                        binding.vaccPercentageFullTv.visibility = View.GONE
                    }
                    val fullyVaccinatedPercentage =
                        calculatePercentage(country.fullyVaccinated, country.population)
                    binding.vaccPercentageFullTv.text =
                        "$fullyVaccinatedPercentage ${getString(R.string.Percentage_fully_vaccinated)}"
                    binding.progressBarFully.progress = fullyVaccinatedPercentage.toInt()

                    if (binding.peopleVaccNumberTv.text.equals(getString(R.string.no_data))) {
                        binding.progressBarFirst.visibility = View.GONE
                        binding.vaccPercentageFirstTv.visibility = View.GONE
                    }
                    val firstVaccinePercentage =
                        calculatePercentage(country.firstVaccine, country.population)
                    binding.vaccPercentageFirstTv.text =
                        "$firstVaccinePercentage ${getString(R.string.Percentage_first_vaccine)}"
                    binding.progressBarFirst.progress = firstVaccinePercentage.toInt()


                    val executor = Executors.newSingleThreadExecutor()
                    executor.execute {
                        countryRepository.updateCountry(country)
                    }

                }
            )

        } catch (e: Exception) {
            val i = Intent(this, NoDataActivity::class.java)
            i.putExtra("country", countryID)
            startActivity(i)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.about_button -> createAlertDialog()
        }
        return true
    }

    private fun createAlertDialog(){

        val dialogBinding: AlertDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.alert_dialog,
            null,
            false
        )

        val alertDialog = AlertDialog.Builder(this, 0).create()

        alertDialog.apply {
            setView(dialogBinding.root)
            setCancelable(false)
        }.show()

        dialogBinding.btnOk.setOnClickListener{
            alertDialog.dismiss()
        }

    }

    private fun calculatePercentage(vaccinated: Int, population: Int): Double =
        ceil((vaccinated / population.toDouble()) * 1000) / 10


    //Formats a number into an easily readable string (Ex: 5000000 to 5,000,000)
    private fun formatNumber(number: Int): String {
        if (number == 0) {
            return "0"
        }
        var numberString = number.toString()
        if (number < 0) {
            numberString = numberString.substring(1)
        }
        var formattedString = ""
        var counter = 1
        for (i in numberString.length - 1 downTo 0) {
            formattedString = numberString[i] + formattedString
            if (counter == 3 && i != 0) {
                formattedString = ",$formattedString"
                counter = 0
            }
            counter++
        }
        if (number < 0) {
            formattedString = "-$formattedString"
        }
        return formattedString
    }

    //Formats a date into a more readable format
    private fun formatDate(unformattedDate: String): String {
        val monthsArray = arrayOf(
            0,
            R.string.January,
            R.string.February,
            R.string.March,
            R.string.April,
            R.string.May,
            R.string.June,
            R.string.July,
            R.string.August,
            R.string.September,
            R.string.October,
            R.string.November,
            R.string.December
        )

        val thirtyDayMonths = listOf(4,6,9,11)

        var dayNumber = unformattedDate.substring(unformattedDate.length - 2).toInt()
        var monthNumber = unformattedDate.get(unformattedDate.length - 4).toString().toInt()
        if(dayNumber == 31){
            dayNumber = 1
            monthNumber++
            if(monthNumber == 13){
                monthNumber = 1
            }
        } else {
            if(dayNumber==28 && monthNumber == 2){
                dayNumber = 1
                monthNumber++
            }
            else if(dayNumber == 30 && thirtyDayMonths.contains(monthNumber)){
                dayNumber = 1
                monthNumber++
            } else {
                dayNumber++
            }
        }
        var day = "$dayNumber."
        if(dayNumber<10){
            day = "0$day"
        }
        val month = getString(monthsArray[monthNumber])
        val year = unformattedDate.substring(0, 4)

        return "$day $month $year"

    }

    //String ID Property der Liste hinzufügen
    private fun findCountryData(countryID: Int): VaccinationData? {
        val countryName = CountryDataSingleton.getEnglishCountryName(countryID)
        for (country in CountryDataSingleton.countryDataList) {
            if (country.country == countryName) {
                return country
            }
        }
        return null
    }

    private fun findCaseData(countryID: Int): Array<CaseInfo?> {
        val caseData = CountryDataSingleton.caseData
        val historicCases = CountryDataSingleton.historicCases
        val historicDeaths = CountryDataSingleton.historicDeaths
        var caseInfo: Array<CaseInfo?> = emptyArray()
        when (countryID) {
            R.string.Afghanistan -> caseInfo = arrayOf(
                caseData?.Afghanistan?.All,
                historicCases?.Afghanistan?.All,
                historicDeaths?.Afghanistan?.All
            )
            R.string.Albania -> caseInfo = arrayOf(
                caseData?.Albania?.All,
                historicCases?.Albania?.All,
                historicDeaths?.Albania?.All
            )
            R.string.Algeria -> caseInfo = arrayOf(
                caseData?.Algeria?.All,
                historicCases?.Algeria?.All,
                historicDeaths?.Algeria?.All
            )
            R.string.AmericanSamoa -> caseInfo = arrayOf(
                caseData?.AmericanSamoa?.All,
                historicCases?.AmericanSamoa?.All,
                historicDeaths?.AmericanSamoa?.All
            )
            //R.string.Andorra -> caseInfo = arrayOf(caseData?.Andorra?.All, historicCases?.Andorra?.All, historicDeaths?.Andorra?.All)
            R.string.Angola -> caseInfo = arrayOf(
                caseData?.Angola?.All,
                historicCases?.Angola?.All,
                historicDeaths?.Angola?.All
            )
            R.string.Anguilla -> caseInfo = arrayOf(
                caseData?.Anguilla?.All,
                historicCases?.Anguilla?.All,
                historicDeaths?.Anguilla?.All
            )
            R.string.Antarctica -> caseInfo = arrayOf(
                caseData?.Antarctica?.All,
                historicCases?.Antarctica?.All,
                historicDeaths?.Antarctica?.All
            )
            R.string.AntiguaandBarbuda -> caseInfo = arrayOf(
                caseData?.AntiguaAndBarbuda?.All,
                historicCases?.AntiguaAndBarbuda?.All,
                historicDeaths?.AntiguaAndBarbuda?.All
            )
            R.string.Argentina -> caseInfo = arrayOf(
                caseData?.Argentina?.All,
                historicCases?.Argentina?.All,
                historicDeaths?.Argentina?.All
            )
            R.string.Armenia -> caseInfo = arrayOf(
                caseData?.Armenia?.All,
                historicCases?.Armenia?.All,
                historicDeaths?.Armenia?.All
            )
            R.string.Aruba -> caseInfo =
                arrayOf(caseData?.Aruba?.All, historicCases?.Aruba?.All, historicDeaths?.Aruba?.All)
            R.string.Australia -> caseInfo = arrayOf(
                caseData?.Australia?.All,
                historicCases?.Australia?.All,
                historicDeaths?.Australia?.All
            )
            R.string.Austria -> caseInfo = arrayOf(
                caseData?.Austria?.All,
                historicCases?.Austria?.All,
                historicDeaths?.Austria?.All
            )
            R.string.Azerbaijan -> caseInfo = arrayOf(
                caseData?.Azerbaijan?.All,
                historicCases?.Azerbaijan?.All,
                historicDeaths?.Azerbaijan?.All
            )
            R.string.Bahamas -> caseInfo = arrayOf(
                caseData?.Bahamas?.All,
                historicCases?.Bahamas?.All,
                historicDeaths?.Bahamas?.All
            )
            R.string.Bahrain -> caseInfo = arrayOf(
                caseData?.Bahrain?.All,
                historicCases?.Bahrain?.All,
                historicDeaths?.Bahrain?.All
            )
            R.string.Bangladesh -> caseInfo = arrayOf(
                caseData?.Bangladesh?.All,
                historicCases?.Bangladesh?.All,
                historicDeaths?.Bangladesh?.All
            )
            R.string.Barbados -> caseInfo = arrayOf(
                caseData?.Barbados?.All,
                historicCases?.Barbados?.All,
                historicDeaths?.Barbados?.All
            )
            R.string.Belarus -> caseInfo = arrayOf(
                caseData?.Belarus?.All,
                historicCases?.Belarus?.All,
                historicDeaths?.Belarus?.All
            )
            R.string.Belgium -> caseInfo = arrayOf(
                caseData?.Belgium?.All,
                historicCases?.Belgium?.All,
                historicDeaths?.Belgium?.All
            )
            R.string.Belize -> caseInfo = arrayOf(
                caseData?.Belize?.All,
                historicCases?.Belize?.All,
                historicDeaths?.Belize?.All
            )
            R.string.Benin -> caseInfo =
                arrayOf(caseData?.Benin?.All, historicCases?.Benin?.All, historicDeaths?.Benin?.All)
            R.string.Bermuda -> caseInfo = arrayOf(
                caseData?.Bermuda?.All,
                historicCases?.Bermuda?.All,
                historicDeaths?.Bermuda?.All
            )
            R.string.Bhutan -> caseInfo = arrayOf(
                caseData?.Bhutan?.All,
                historicCases?.Bhutan?.All,
                historicDeaths?.Bhutan?.All
            )
            R.string.Bolivia -> caseInfo = arrayOf(
                caseData?.Bolivia?.All,
                historicCases?.Bolivia?.All,
                historicDeaths?.Bolivia?.All
            )
            R.string.BosniaandHerzegovina -> caseInfo = arrayOf(
                caseData?.BosniaAndHerzegovina?.All,
                historicCases?.BosniaAndHerzegovina?.All,
                historicDeaths?.BosniaAndHerzegovina?.All
            )
            R.string.Botswana -> caseInfo = arrayOf(
                caseData?.Botswana?.All,
                historicCases?.Botswana?.All,
                historicDeaths?.Botswana?.All
            )
            R.string.BouvetIsland -> caseInfo = arrayOf(
                caseData?.BouvetIsland?.All,
                historicCases?.BouvetIsland?.All,
                historicDeaths?.BouvetIsland?.All
            )
            R.string.Brazil -> caseInfo = arrayOf(
                caseData?.Brazil?.All,
                historicCases?.Brazil?.All,
                historicDeaths?.Brazil?.All
            )
            //R.string.BritishIndianOceanTerritory -> caseInfo = arrayOf(caseData?.BritishIndianOceanTerritory?.All, historicCases?.BritishIndianOceanTerritory?.All, historicDeaths?.BritishIndianOceanTerritory?.All)
            R.string.BruneiDarussalam -> caseInfo = arrayOf(
                caseData?.Brunei?.All,
                historicCases?.Brunei?.All,
                historicDeaths?.Brunei?.All
            )
            R.string.Bulgaria -> caseInfo = arrayOf(
                caseData?.Bulgaria?.All,
                historicCases?.Bulgaria?.All,
                historicDeaths?.Bulgaria?.All
            )
            R.string.BurkinaFaso -> caseInfo = arrayOf(
                caseData?.BurkinaFaso?.All,
                historicCases?.BurkinaFaso?.All,
                historicDeaths?.BurkinaFaso?.All
            )
            R.string.Burundi -> caseInfo = arrayOf(
                caseData?.Burundi?.All,
                historicCases?.Burundi?.All,
                historicDeaths?.Burundi?.All
            )
            R.string.Cambodia -> caseInfo = arrayOf(
                caseData?.Cambodia?.All,
                historicCases?.Cambodia?.All,
                historicDeaths?.Cambodia?.All
            )
            R.string.Cameroon -> caseInfo = arrayOf(
                caseData?.Cameroon?.All,
                historicCases?.Cameroon?.All,
                historicDeaths?.Cameroon?.All
            )
            R.string.Canada -> caseInfo = arrayOf(
                caseData?.Canada?.All,
                historicCases?.Canada?.All,
                historicDeaths?.Canada?.All
            )
            R.string.CapeVerde -> caseInfo = arrayOf(
                caseData?.CapeVerde?.All,
                historicCases?.CapeVerde?.All,
                historicDeaths?.CapeVerde?.All
            )
            R.string.CaymanIslands -> caseInfo = arrayOf(
                caseData?.CaymanIslands?.All,
                historicCases?.CaymanIslands?.All,
                historicDeaths?.CaymanIslands?.All
            )
            R.string.CentralAfricanRepublic -> caseInfo = arrayOf(
                caseData?.CentralAfricanRepublic?.All,
                historicCases?.CentralAfricanRepublic?.All,
                historicDeaths?.CentralAfricanRepublic?.All
            )
            R.string.Chad -> caseInfo =
                arrayOf(caseData?.Chad?.All, historicCases?.Chad?.All, historicDeaths?.Chad?.All)
            R.string.Chile -> caseInfo =
                arrayOf(caseData?.Chile?.All, historicCases?.Chile?.All, historicDeaths?.Chile?.All)
            R.string.China -> caseInfo =
                arrayOf(caseData?.China?.All, historicCases?.China?.All, historicDeaths?.China?.All)
            R.string.ChristmasIsland -> caseInfo = arrayOf(
                caseData?.ChristmasIsland?.All,
                historicCases?.ChristmasIsland?.All,
                historicDeaths?.ChristmasIsland?.All
            )
            R.string.CocosIslands -> caseInfo =
                arrayOf(caseData?.Cocos?.All, historicCases?.Cocos?.All, historicDeaths?.Cocos?.All)
            R.string.Colombia -> caseInfo = arrayOf(
                caseData?.Colombia?.All,
                historicCases?.Colombia?.All,
                historicDeaths?.Colombia?.All
            )
            R.string.Comoros -> caseInfo = arrayOf(
                caseData?.Comoros?.All,
                historicCases?.Comoros?.All,
                historicDeaths?.Comoros?.All
            )
            R.string.Congo -> caseInfo = arrayOf(
                caseData?.Congo?.All,
                historicCases?.Congo?.All,
                historicDeaths?.Congo?.All
            )
            R.string.DRCongoe -> caseInfo = arrayOf(
                caseData?.CongoDR?.All,
                historicCases?.CongoDR?.All,
                historicDeaths?.CongoDR?.All
            )
            R.string.CookIslands -> caseInfo = arrayOf(
                caseData?.CookIslands?.All,
                historicCases?.CookIslands?.All,
                historicDeaths?.CookIslands?.All
            )
            R.string.CostaRica -> caseInfo = arrayOf(
                caseData?.CostaRica?.All,
                historicCases?.CostaRica?.All,
                historicDeaths?.CostaRica?.All
            )
            R.string.CoteDIvoire -> caseInfo = arrayOf(
                caseData?.CoteDIvoire?.All,
                historicCases?.CoteDIvoire?.All,
                historicDeaths?.CoteDIvoire?.All
            )
            R.string.Croatia -> caseInfo = arrayOf(
                caseData?.Croatia?.All,
                historicCases?.Croatia?.All,
                historicDeaths?.Croatia?.All
            )
            R.string.Cuba -> caseInfo =
                arrayOf(caseData?.Cuba?.All, historicCases?.Cuba?.All, historicDeaths?.Cuba?.All)
            R.string.Cyprus -> caseInfo = arrayOf(
                caseData?.Cyprus?.All,
                historicCases?.Cyprus?.All,
                historicDeaths?.Cyprus?.All
            )
            R.string.CzechRepublic -> caseInfo = arrayOf(
                caseData?.Czechia?.All,
                historicCases?.Czechia?.All,
                historicDeaths?.Czechia?.All
            )
            R.string.Denmark -> caseInfo = arrayOf(
                caseData?.Denmark?.All,
                historicCases?.Denmark?.All,
                historicDeaths?.Denmark?.All
            )
            R.string.Djibouti -> caseInfo = arrayOf(
                caseData?.Djibouti?.All,
                historicCases?.Djibouti?.All,
                historicDeaths?.Djibouti?.All
            )
            R.string.Dominica -> caseInfo = arrayOf(
                caseData?.Dominica?.All,
                historicCases?.Dominica?.All,
                historicDeaths?.Dominica?.All
            )
            R.string.DominicanRepublic -> caseInfo = arrayOf(
                caseData?.DominicanRepublic?.All,
                historicCases?.DominicanRepublic?.All,
                historicDeaths?.DominicanRepublic?.All
            )
//            R.string.EastTimor -> caseInfo = arrayOf(
//                caseData?.TimorLeste?.All,
//                historicCases?.TimorLeste?.All,
//                historicDeaths?.TimorLeste?.All
//            )
            R.string.Ecuador -> caseInfo = arrayOf(
                caseData?.Ecuador?.All,
                historicCases?.Ecuador?.All,
                historicDeaths?.Ecuador?.All
            )
            R.string.Egypt -> caseInfo =
                arrayOf(caseData?.Egypt?.All, historicCases?.Egypt?.All, historicDeaths?.Egypt?.All)
            R.string.ElSalvador -> caseInfo = arrayOf(
                caseData?.ElSalvador?.All,
                historicCases?.ElSalvador?.All,
                historicDeaths?.ElSalvador?.All
            )
            R.string.EquatorialGuinea -> caseInfo = arrayOf(
                caseData?.EquatorialGuinea?.All,
                historicCases?.EquatorialGuinea?.All,
                historicDeaths?.EquatorialGuinea?.All
            )
            R.string.Eritrea -> caseInfo = arrayOf(
                caseData?.Eritrea?.All,
                historicCases?.Eritrea?.All,
                historicDeaths?.Eritrea?.All
            )
            R.string.Estonia -> caseInfo = arrayOf(
                caseData?.Estonia?.All,
                historicCases?.Estonia?.All,
                historicDeaths?.Estonia?.All
            )
            R.string.Eswatini -> caseInfo = arrayOf(
                caseData?.Eswatini?.All,
                historicCases?.Eswatini?.All,
                historicDeaths?.Eswatini?.All
            )
            R.string.Ethiopia -> caseInfo = arrayOf(
                caseData?.Ethiopia?.All,
                historicCases?.Ethiopia?.All,
                historicDeaths?.Ethiopia?.All
            )
            R.string.FalklandIslands -> caseInfo = arrayOf(
                caseData?.FalklandIslands?.All,
                historicCases?.FalklandIslands?.All,
                historicDeaths?.FalklandIslands?.All
            )
            R.string.FaroeIslands -> caseInfo = arrayOf(
                caseData?.FaroeIslands?.All,
                historicCases?.FaroeIslands?.All,
                historicDeaths?.FaroeIslands?.All
            )
            R.string.Fiji -> caseInfo =
                arrayOf(caseData?.Fiji?.All, historicCases?.Fiji?.All, historicDeaths?.Fiji?.All)
            R.string.Finland -> caseInfo = arrayOf(
                caseData?.Finland?.All,
                historicCases?.Finland?.All,
                historicDeaths?.Finland?.All
            )
            R.string.France -> caseInfo = arrayOf(
                caseData?.France?.All,
                historicCases?.France?.All,
                historicDeaths?.France?.All
            )
            //R.string.FranceMetropolitan -> caseInfo = arrayOf(caseData?.FranceMetropolitan?.All, historicCases?.FranceMetropolitan?.All, historicDeaths?.FranceMetropolitan?.All)
            R.string.FrenchGuiana -> caseInfo = arrayOf(
                caseData?.FrenchGuiana?.All,
                historicCases?.FrenchGuiana?.All,
                historicDeaths?.FrenchGuiana?.All
            )
            R.string.FrenchPolynesia -> caseInfo = arrayOf(
                caseData?.FrenchPolynesia?.All,
                historicCases?.FrenchPolynesia?.All,
                historicDeaths?.FrenchPolynesia?.All
            )
            //R.string.FrenchSouthernTerritories -> caseInfo = arrayOf(caseData?.FrenchSouthernTerritories?.All, historicCases?.FrenchSouthernTerritories?.All, historicDeaths?.FrenchSouthernTerritories?.All)
            R.string.Gabon -> caseInfo =
                arrayOf(caseData?.Gabon?.All, historicCases?.Gabon?.All, historicDeaths?.Gabon?.All)
            R.string.Gambia -> caseInfo = arrayOf(
                caseData?.Gambia?.All,
                historicCases?.Gambia?.All,
                historicDeaths?.Gambia?.All
            )
            R.string.Georgia -> caseInfo = arrayOf(
                caseData?.Georgia?.All,
                historicCases?.Georgia?.All,
                historicDeaths?.Georgia?.All
            )
            R.string.Germany -> caseInfo = arrayOf(
                caseData?.Germany?.All,
                historicCases?.Germany?.All,
                historicDeaths?.Germany?.All
            )
            R.string.Ghana -> caseInfo =
                arrayOf(caseData?.Ghana?.All, historicCases?.Ghana?.All, historicDeaths?.Ghana?.All)
            R.string.Gibraltar -> caseInfo = arrayOf(
                caseData?.Gibraltar?.All,
                historicCases?.Gibraltar?.All,
                historicDeaths?.Gibraltar?.All
            )
            R.string.Greece -> caseInfo = arrayOf(
                caseData?.Greece?.All,
                historicCases?.Greece?.All,
                historicDeaths?.Greece?.All
            )
            R.string.Greenland -> caseInfo = arrayOf(
                caseData?.Greenland?.All,
                historicCases?.Greenland?.All,
                historicDeaths?.Greenland?.All
            )
            R.string.Grenada -> caseInfo = arrayOf(
                caseData?.Grenada?.All,
                historicCases?.Grenada?.All,
                historicDeaths?.Grenada?.All
            )
            R.string.Guadeloupe -> caseInfo = arrayOf(
                caseData?.Guadeloupe?.All,
                historicCases?.Guadeloupe?.All,
                historicDeaths?.Guadeloupe?.All
            )
            R.string.Guam -> caseInfo =
                arrayOf(caseData?.Guam?.All, historicCases?.Guam?.All, historicDeaths?.Guam?.All)
            R.string.Guatemala -> caseInfo = arrayOf(
                caseData?.Guatemala?.All,
                historicCases?.Guatemala?.All,
                historicDeaths?.Guatemala?.All
            )
            R.string.Guinea -> caseInfo = arrayOf(
                caseData?.Guinea?.All,
                historicCases?.Guinea?.All,
                historicDeaths?.Guinea?.All
            )
            R.string.GuineaBissau -> caseInfo = arrayOf(
                caseData?.GuineaBissau?.All,
                historicCases?.GuineaBissau?.All,
                historicDeaths?.GuineaBissau?.All
            )
            R.string.Guyana -> caseInfo = arrayOf(
                caseData?.Guyana?.All,
                historicCases?.Guyana?.All,
                historicDeaths?.Guyana?.All
            )
            R.string.Haiti -> caseInfo =
                arrayOf(caseData?.Haiti?.All, historicCases?.Haiti?.All, historicDeaths?.Haiti?.All)
            //R.string.HeardandMcDonaldIslands -> caseInfo = arrayOf(caseData?.HeardAndMcDonaldIslands?.All, historicCases?.HeardAndMcDonaldIslands?.All, historicDeaths?.HeardAndMcDonaldIslands?.All)
            //R.string.HolySee -> caseInfo = arrayOf(caseData?.HolySee?.All, historicCases?.HolySee?.All, historicDeaths?.HolySee?.All)
            R.string.Honduras -> caseInfo = arrayOf(
                caseData?.Honduras?.All,
                historicCases?.Honduras?.All,
                historicDeaths?.Honduras?.All
            )
            R.string.HongKong -> caseInfo = arrayOf(
                caseData?.HongKong?.All,
                historicCases?.HongKong?.All,
                historicDeaths?.HongKong?.All
            )
            R.string.Hungary -> caseInfo = arrayOf(
                caseData?.Hungary?.All,
                historicCases?.Hungary?.All,
                historicDeaths?.Hungary?.All
            )
            R.string.Iceland -> caseInfo = arrayOf(
                caseData?.Iceland?.All,
                historicCases?.Iceland?.All,
                historicDeaths?.Iceland?.All
            )
            R.string.India -> caseInfo =
                arrayOf(caseData?.India?.All, historicCases?.India?.All, historicDeaths?.India?.All)
            R.string.Indonesia -> caseInfo = arrayOf(
                caseData?.Indonesia?.All,
                historicCases?.Indonesia?.All,
                historicDeaths?.Indonesia?.All
            )
            R.string.Iran -> caseInfo =
                arrayOf(caseData?.Iran?.All, historicCases?.Iran?.All, historicDeaths?.Iran?.All)
            R.string.Iraq -> caseInfo =
                arrayOf(caseData?.Iraq?.All, historicCases?.Iraq?.All, historicDeaths?.Iraq?.All)
            R.string.Ireland -> caseInfo = arrayOf(
                caseData?.Ireland?.All,
                historicCases?.Ireland?.All,
                historicDeaths?.Ireland?.All
            )
            R.string.Israel -> caseInfo = arrayOf(
                caseData?.Israel?.All,
                historicCases?.Israel?.All,
                historicDeaths?.Israel?.All
            )
            R.string.Italy -> caseInfo =
                arrayOf(caseData?.Italy?.All, historicCases?.Italy?.All, historicDeaths?.Italy?.All)
            R.string.Jamaica -> caseInfo = arrayOf(
                caseData?.Jamaica?.All,
                historicCases?.Jamaica?.All,
                historicDeaths?.Jamaica?.All
            )
            R.string.Japan -> caseInfo =
                arrayOf(caseData?.Japan?.All, historicCases?.Japan?.All, historicDeaths?.Japan?.All)
            R.string.Jordan -> caseInfo = arrayOf(
                caseData?.Jordan?.All,
                historicCases?.Jordan?.All,
                historicDeaths?.Jordan?.All
            )
            R.string.Kazakhstan -> caseInfo = arrayOf(
                caseData?.Kazakhstan?.All,
                historicCases?.Kazakhstan?.All,
                historicDeaths?.Kazakhstan?.All
            )
            R.string.Kenya -> caseInfo =
                arrayOf(caseData?.Kenya?.All, historicCases?.Kenya?.All, historicDeaths?.Kenya?.All)
            R.string.Kiribati -> caseInfo = arrayOf(
                caseData?.Kiribati?.All,
                historicCases?.Kiribati?.All,
                historicDeaths?.Kiribati?.All
            )
            R.string.NorthKorea -> caseInfo = arrayOf(
                caseData?.NorthKorea?.All,
                historicCases?.NorthKorea?.All,
                historicDeaths?.NorthKorea?.All
            )
            R.string.SouthKorea -> caseInfo = arrayOf(
                caseData?.SouthKorea?.All,
                historicCases?.SouthKorea?.All,
                historicDeaths?.SouthKorea?.All
            )
            R.string.Kuwait -> caseInfo = arrayOf(
                caseData?.Kuwait?.All,
                historicCases?.Kuwait?.All,
                historicDeaths?.Kuwait?.All
            )
            R.string.Kyrgyzstan -> caseInfo = arrayOf(
                caseData?.Kyrgyzstan?.All,
                historicCases?.Kyrgyzstan?.All,
                historicDeaths?.Kyrgyzstan?.All
            )
            R.string.Laos -> caseInfo =
                arrayOf(caseData?.Laos?.All, historicDeaths?.Laos?.All, historicDeaths?.Laos?.All)
            R.string.Latvia -> caseInfo = arrayOf(
                caseData?.Latvia?.All,
                historicCases?.Latvia?.All,
                historicDeaths?.Latvia?.All
            )
            R.string.Lebanon -> caseInfo = arrayOf(
                caseData?.Lebanon?.All,
                historicCases?.Lebanon?.All,
                historicDeaths?.Lebanon?.All
            )
            R.string.Lesotho -> caseInfo = arrayOf(
                caseData?.Lesotho?.All,
                historicCases?.Lesotho?.All,
                historicDeaths?.Lesotho?.All
            )
            R.string.Liberia -> caseInfo = arrayOf(
                caseData?.Liberia?.All,
                historicCases?.Liberia?.All,
                historicDeaths?.Liberia?.All
            )
            R.string.Libya -> caseInfo =
                arrayOf(caseData?.Libya?.All, historicCases?.Libya?.All, historicDeaths?.Libya?.All)
            R.string.Liechtenstein -> caseInfo = arrayOf(
                caseData?.Liechtenstein?.All,
                historicCases?.Liechtenstein?.All,
                historicDeaths?.Liechtenstein?.All
            )
            R.string.Lithuania -> caseInfo = arrayOf(
                caseData?.Lithuania?.All,
                historicCases?.Lithuania?.All,
                historicDeaths?.Lithuania?.All
            )
            R.string.Luxembourg -> caseInfo = arrayOf(
                caseData?.Luxembourg?.All,
                historicCases?.Luxembourg?.All,
                historicDeaths?.Luxembourg?.All
            )
            R.string.Macau -> caseInfo =
                arrayOf(caseData?.Macau?.All, historicCases?.Macau?.All, historicDeaths?.Macau?.All)
            R.string.NorthMacedonia -> caseInfo = arrayOf(
                caseData?.Macedonia?.All,
                historicCases?.Macedonia?.All,
                historicDeaths?.Macedonia?.All
            )
            R.string.Madagascar -> caseInfo = arrayOf(
                caseData?.Madagascar?.All,
                historicCases?.Madagascar?.All,
                historicDeaths?.Madagascar?.All
            )
            R.string.Malawi -> caseInfo = arrayOf(
                caseData?.Malawi?.All,
                historicCases?.Malawi?.All,
                historicDeaths?.Malawi?.All
            )
            R.string.Malaysia -> caseInfo = arrayOf(
                caseData?.Malaysia?.All,
                historicCases?.Malaysia?.All,
                historicDeaths?.Malaysia?.All
            )
            R.string.Maldives -> caseInfo = arrayOf(
                caseData?.Maldives?.All,
                historicCases?.Maldives?.All,
                historicDeaths?.Maldives?.All
            )
            R.string.Mali -> caseInfo =
                arrayOf(caseData?.Mali?.All, historicCases?.Mali?.All, historicDeaths?.Mali?.All)
            R.string.Malta -> caseInfo =
                arrayOf(caseData?.Malta?.All, historicCases?.Malta?.All, historicDeaths?.Malta?.All)
            R.string.MarshallIslands -> caseInfo = arrayOf(
                caseData?.MarshallIslands?.All,
                historicCases?.MarshallIslands?.All,
                historicDeaths?.MarshallIslands?.All
            )
            R.string.Martinique -> caseInfo = arrayOf(
                caseData?.Martinique?.All,
                historicCases?.Martinique?.All,
                historicDeaths?.Martinique?.All
            )
            R.string.Mauritania -> caseInfo = arrayOf(
                caseData?.Mauritania?.All,
                historicCases?.Mauritania?.All,
                historicDeaths?.Mauritania?.All
            )
            R.string.Mauritius -> caseInfo = arrayOf(
                caseData?.Mauritius?.All,
                historicCases?.Mauritius?.All,
                historicDeaths?.Mauritius?.All
            )
            R.string.Mayotte -> caseInfo = arrayOf(
                caseData?.Mayotte?.All,
                historicCases?.Mayotte?.All,
                historicDeaths?.Mayotte?.All
            )
            R.string.Mexico -> caseInfo = arrayOf(
                caseData?.Mexico?.All,
                historicCases?.Mexico?.All,
                historicDeaths?.Mexico?.All
            )
            R.string.Micronesia -> caseInfo = arrayOf(
                caseData?.Micronesia?.All,
                historicCases?.Micronesia?.All,
                historicDeaths?.Micronesia?.All
            )
            R.string.Moldova -> caseInfo = arrayOf(
                caseData?.Moldova?.All,
                historicCases?.Moldova?.All,
                historicDeaths?.Moldova?.All
            )
            R.string.Monaco -> caseInfo = arrayOf(
                caseData?.Monaco?.All,
                historicCases?.Monaco?.All,
                historicDeaths?.Monaco?.All
            )
            R.string.Mongolia -> caseInfo = arrayOf(
                caseData?.Mongolia?.All,
                historicCases?.Mongolia?.All,
                historicDeaths?.Mongolia?.All
            )
            R.string.Montenegro -> caseInfo = arrayOf(
                caseData?.Montenegro?.All,
                historicCases?.Montenegro?.All,
                historicDeaths?.Montenegro?.All
            )
            R.string.Montserrat -> caseInfo = arrayOf(
                caseData?.Montserrat?.All,
                historicCases?.Montserrat?.All,
                historicDeaths?.Montserrat?.All
            )
            R.string.Morocco -> caseInfo = arrayOf(
                caseData?.Morocco?.All,
                historicCases?.Morocco?.All,
                historicDeaths?.Morocco?.All
            )
            R.string.Mozambique -> caseInfo = arrayOf(
                caseData?.Mozambique?.All,
                historicCases?.Mozambique?.All,
                historicDeaths?.Mozambique?.All
            )
            R.string.Myanmar -> caseInfo = arrayOf(
                caseData?.Myanmar?.All,
                historicCases?.Myanmar?.All,
                historicDeaths?.Myanmar?.All
            )
            R.string.Namibia -> caseInfo = arrayOf(
                caseData?.Namibia?.All,
                historicCases?.Namibia?.All,
                historicDeaths?.Namibia?.All
            )
            R.string.Nauru -> caseInfo =
                arrayOf(caseData?.Nauru?.All, historicCases?.Nauru?.All, historicDeaths?.Nauru?.All)
            R.string.Nepal -> caseInfo =
                arrayOf(caseData?.Nepal?.All, historicCases?.Nepal?.All, historicDeaths?.Nepal?.All)
            R.string.Netherlands -> caseInfo = arrayOf(
                caseData?.Netherlands?.All,
                historicCases?.Netherlands?.All,
                historicDeaths?.Netherlands?.All
            )
            R.string.NetherlandsAntilles -> caseInfo = arrayOf(
                caseData?.NetherlandsAntilles?.All,
                historicCases?.NetherlandsAntilles?.All,
                historicDeaths?.NetherlandsAntilles?.All
            )
            R.string.NewCaledonia -> caseInfo = arrayOf(
                caseData?.NewCaledonia?.All,
                historicCases?.NewCaledonia?.All,
                historicDeaths?.NewCaledonia?.All
            )
            R.string.NewZealand -> caseInfo = arrayOf(
                caseData?.NewZealand?.All,
                historicCases?.NewZealand?.All,
                historicDeaths?.NewZealand?.All
            )
            R.string.Nicaragua -> caseInfo = arrayOf(
                caseData?.Nicaragua?.All,
                historicCases?.Nicaragua?.All,
                historicDeaths?.Nicaragua?.All
            )
            R.string.Niger -> caseInfo =
                arrayOf(caseData?.Niger?.All, historicCases?.Niger?.All, historicDeaths?.Niger?.All)
            R.string.Nigeria -> caseInfo = arrayOf(
                caseData?.Nigeria?.All,
                historicCases?.Nigeria?.All,
                historicDeaths?.Nigeria?.All
            )
            R.string.Niue -> caseInfo =
                arrayOf(caseData?.Niue?.All, historicCases?.Niue?.All, historicDeaths?.Niue?.All)
            R.string.NorfolkIsland -> caseInfo = arrayOf(
                caseData?.NorfolkIsland?.All,
                historicCases?.NorfolkIsland?.All,
                historicDeaths?.NorfolkIsland?.All
            )
            R.string.NorthernMarianaIslands -> caseInfo = arrayOf(
                caseData?.NorthernMarianaIslands?.All,
                historicCases?.NorthernMarianaIslands?.All,
                historicDeaths?.NorthernMarianaIslands?.All
            )
            R.string.NorthMacedonia -> caseInfo = arrayOf(
                caseData?.NorthMacedonia?.All,
                historicCases?.NorthMacedonia?.All,
                historicDeaths?.NorthMacedonia?.All
            )
            R.string.Norway -> caseInfo = arrayOf(
                caseData?.Norway?.All,
                historicCases?.Norway?.All,
                historicDeaths?.Norway?.All
            )
            R.string.Oman -> caseInfo =
                arrayOf(caseData?.Oman?.All, historicCases?.Oman?.All, historicDeaths?.Oman?.All)
            R.string.Pakistan -> caseInfo = arrayOf(
                caseData?.Pakistan?.All,
                historicCases?.Pakistan?.All,
                historicDeaths?.Pakistan?.All
            )
            R.string.Palau -> caseInfo =
                arrayOf(caseData?.Palau?.All, historicCases?.Palau?.All, historicDeaths?.Palau?.All)
            R.string.Panama -> caseInfo = arrayOf(
                caseData?.Panama?.All,
                historicCases?.Panama?.All,
                historicDeaths?.Panama?.All
            )
            R.string.PapuaNewGuinea -> caseInfo = arrayOf(
                caseData?.PapuaNewGuinea?.All,
                historicCases?.PapuaNewGuinea?.All,
                historicDeaths?.PapuaNewGuinea?.All
            )
            R.string.Paraguay -> caseInfo = arrayOf(
                caseData?.Paraguay?.All,
                historicCases?.Paraguay?.All,
                historicDeaths?.Paraguay?.All
            )
            R.string.Peru -> caseInfo =
                arrayOf(caseData?.Peru?.All, historicCases?.Peru?.All, historicDeaths?.Peru?.All)
            R.string.Philippines -> caseInfo = arrayOf(
                caseData?.Philippines?.All,
                historicCases?.Philippines?.All,
                historicDeaths?.Philippines?.All
            )
            R.string.Pitcairn -> caseInfo = arrayOf(
                caseData?.Pitcairn?.All,
                historicCases?.Pitcairn?.All,
                historicDeaths?.Pitcairn?.All
            )
            R.string.Poland -> caseInfo = arrayOf(
                caseData?.Poland?.All,
                historicCases?.Poland?.All,
                historicDeaths?.Poland?.All
            )
            R.string.Portugal -> caseInfo = arrayOf(
                caseData?.Portugal?.All,
                historicCases?.Portugal?.All,
                historicDeaths?.Portugal?.All
            )
            R.string.PuertoRico -> caseInfo = arrayOf(
                caseData?.PuertoRico?.All,
                historicCases?.PuertoRico?.All,
                historicDeaths?.PuertoRico?.All
            )
            R.string.Qatar -> caseInfo =
                arrayOf(caseData?.Qatar?.All, historicCases?.Qatar?.All, historicDeaths?.Qatar?.All)
            R.string.Reunion -> caseInfo = arrayOf(
                caseData?.Reunion?.All,
                historicCases?.Reunion?.All,
                historicDeaths?.Reunion?.All
            )
            R.string.Romania -> caseInfo = arrayOf(
                caseData?.Romania?.All,
                historicCases?.Romania?.All,
                historicDeaths?.Romania?.All
            )
            R.string.Russia -> caseInfo = arrayOf(
                caseData?.Russia?.All,
                historicCases?.Russia?.All,
                historicDeaths?.Russia?.All
            )
            R.string.Rwanda -> caseInfo = arrayOf(
                caseData?.Rwanda?.All,
                historicCases?.Rwanda?.All,
                historicDeaths?.Rwanda?.All
            )
            R.string.SaintKittsandNevis -> caseInfo = arrayOf(
                caseData?.SaintKittsAndNevis?.All,
                historicCases?.SaintKittsAndNevis?.All,
                historicDeaths?.SaintKittsAndNevis?.All
            )
            R.string.SaintLucia -> caseInfo = arrayOf(
                caseData?.SaintLucia?.All,
                historicCases?.SaintLucia?.All,
                historicDeaths?.SaintLucia?.All
            )
            R.string.SaintVincentandtheGrenadines -> caseInfo = arrayOf(
                caseData?.SaintVincentAndTheGrenadines?.All,
                historicCases?.SaintVincentAndTheGrenadines?.All,
                historicDeaths?.SaintVincentAndTheGrenadines?.All
            )
            R.string.Samoa -> caseInfo =
                arrayOf(caseData?.Samoa?.All, historicCases?.Samoa?.All, historicDeaths?.Samoa?.All)
            R.string.SanMarino -> caseInfo = arrayOf(
                caseData?.SanMarino?.All,
                historicCases?.SanMarino?.All,
                historicDeaths?.SanMarino?.All
            )
            //R.string.SaoTomeAndPrincipe -> caseInfo = arrayOf(caseData?.SaoTomeAndPrincipe?.All, historicCases?.SaoTomeAndPrincipe?.All, historicDeaths?.SaoTomeAndPrincipe?.All)
            R.string.SaudiArabia -> caseInfo = arrayOf(
                caseData?.SaudiArabia?.All,
                historicCases?.SaudiArabia?.All,
                historicDeaths?.SaudiArabia?.All
            )
            R.string.Senegal -> caseInfo = arrayOf(
                caseData?.Senegal?.All,
                historicCases?.Senegal?.All,
                historicDeaths?.Senegal?.All
            )
            R.string.Serbia -> caseInfo = arrayOf(
                caseData?.Serbia?.All,
                historicCases?.Serbia?.All,
                historicDeaths?.Serbia?.All
            )
            R.string.Seychelles -> caseInfo = arrayOf(
                caseData?.Seychelles?.All,
                historicCases?.Seychelles?.All,
                historicDeaths?.Seychelles?.All
            )
            R.string.SierraLeone -> caseInfo = arrayOf(
                caseData?.SierraLeone?.All,
                historicCases?.SierraLeone?.All,
                historicDeaths?.SierraLeone?.All
            )
            R.string.Singapore -> caseInfo = arrayOf(
                caseData?.Singapore?.All,
                historicCases?.Singapore?.All,
                historicDeaths?.Singapore?.All
            )
            R.string.Slovakia -> caseInfo = arrayOf(
                caseData?.Slovakia?.All,
                historicCases?.Slovakia?.All,
                historicDeaths?.Slovakia?.All
            )
            R.string.Slovenia -> caseInfo = arrayOf(
                caseData?.Slovenia?.All,
                historicCases?.Slovenia?.All,
                historicDeaths?.Slovenia?.All
            )
            R.string.SolomonIslands -> caseInfo = arrayOf(
                caseData?.SolomonIslands?.All,
                historicCases?.SolomonIslands?.All,
                historicDeaths?.SolomonIslands?.All
            )
            R.string.Somalia -> caseInfo = arrayOf(
                caseData?.Somalia?.All,
                historicCases?.Somalia?.All,
                historicDeaths?.Somalia?.All
            )
            R.string.SouthAfrica -> caseInfo = arrayOf(
                caseData?.SouthAfrica?.All,
                historicCases?.SouthAfrica?.All,
                historicDeaths?.SouthAfrica?.All
            )
            //R.string.SouthGeorgiaandtheSouthSandwichIslands -> caseInfo = arrayOf(caseData?.SouthGeorgiaAndTheSouthSandwichIslands?.All, historicCases?.SouthGeorgiaAndTheSouthSandwichIslands?.All, historicDeaths?.SouthGeorgiaAndTheSouthSandwichIslands?.All)
            R.string.Spain -> caseInfo =
                arrayOf(caseData?.Spain?.All, historicCases?.Spain?.All, historicDeaths?.Spain?.All)
            R.string.SriLanka -> caseInfo = arrayOf(
                caseData?.SriLanka?.All,
                historicCases?.SriLanka?.All,
                historicDeaths?.SriLanka?.All
            )
            R.string.StHelena -> caseInfo = arrayOf(
                caseData?.StHelena?.All,
                historicCases?.StHelena?.All,
                historicDeaths?.StHelena?.All
            )
            R.string.StPierreandMiquelon -> caseInfo = arrayOf(
                caseData?.StPierreAndMiquelon?.All,
                historicCases?.StPierreAndMiquelon?.All,
                historicDeaths?.StPierreAndMiquelon?.All
            )
            R.string.Sudan -> caseInfo =
                arrayOf(caseData?.Sudan?.All, historicCases?.Sudan?.All, historicDeaths?.Sudan?.All)
            R.string.Suriname -> caseInfo = arrayOf(
                caseData?.Suriname?.All,
                historicCases?.Suriname?.All,
                historicDeaths?.Suriname?.All
            )
            //R.string.SvalbardandJanMayenIslands -> caseInfo = arrayOf(caseData?.SvalbardAndJanMayenIslands?.All, historicCases?.SvalbardAndJanMayenIslands?.All, historicDeaths?.SvalbardAndJanMayenIslands?.All)
            R.string.Swaziland -> caseInfo = arrayOf(
                caseData?.Swaziland?.All,
                historicCases?.Swaziland?.All,
                historicDeaths?.Swaziland?.All
            )
            R.string.Sweden -> caseInfo = arrayOf(
                caseData?.Sweden?.All,
                historicCases?.Sweden?.All,
                historicDeaths?.Sweden?.All
            )
            R.string.Switzerland -> caseInfo = arrayOf(
                caseData?.Switzerland?.All,
                historicCases?.Switzerland?.All,
                historicDeaths?.Switzerland?.All
            )
            R.string.Syria -> caseInfo =
                arrayOf(caseData?.Syria?.All, historicCases?.Syria?.All, historicDeaths?.Syria?.All)
            R.string.Taiwan -> caseInfo = arrayOf(
                caseData?.Taiwan?.All,
                historicCases?.Taiwan?.All,
                historicDeaths?.Taiwan?.All
            )
            R.string.Tajikistan -> caseInfo = arrayOf(
                caseData?.Tajikistan?.All,
                historicCases?.Tajikistan?.All,
                historicDeaths?.Tajikistan?.All
            )
            R.string.Tanzania -> caseInfo = arrayOf(
                caseData?.Tanzania?.All,
                historicCases?.Tanzania?.All,
                historicDeaths?.Tanzania?.All
            )
            R.string.Thailand -> caseInfo = arrayOf(
                caseData?.Thailand?.All,
                historicCases?.Thailand?.All,
                historicDeaths?.Thailand?.All
            )
            R.string.EastTimor -> caseInfo = arrayOf(
                caseData?.TimorLeste?.All,
                historicCases?.TimorLeste?.All,
                historicDeaths?.TimorLeste?.All
            )
            R.string.Togo -> caseInfo =
                arrayOf(caseData?.Togo?.All, historicCases?.Togo?.All, historicDeaths?.Togo?.All)
            R.string.Tokelau -> caseInfo = arrayOf(
                caseData?.Tokelau?.All,
                historicCases?.Tokelau?.All,
                historicDeaths?.Tokelau?.All
            )
            R.string.Tonga -> caseInfo =
                arrayOf(caseData?.Tonga?.All, historicCases?.Tonga?.All, historicDeaths?.Tonga?.All)
            R.string.TrinidadandTobago -> caseInfo = arrayOf(
                caseData?.TrinidadAndTobago?.All,
                historicCases?.TrinidadAndTobago?.All,
                historicDeaths?.TrinidadAndTobago?.All
            )
            R.string.Tunisia -> caseInfo = arrayOf(
                caseData?.Tunisia?.All,
                historicCases?.Tunisia?.All,
                historicDeaths?.Tunisia?.All
            )
            R.string.Turkey -> caseInfo = arrayOf(
                caseData?.Turkey?.All,
                historicCases?.Turkey?.All,
                historicDeaths?.Turkey?.All
            )
            R.string.Turkmenistan -> caseInfo = arrayOf(
                caseData?.Turkmenistan?.All,
                historicCases?.Turkmenistan?.All,
                historicDeaths?.Turkmenistan?.All
            )
            R.string.TurksandCaicosIslands -> caseInfo = arrayOf(
                caseData?.TurksAndCaicosIslands?.All,
                historicCases?.TurksAndCaicosIslands?.All,
                historicDeaths?.TurksAndCaicosIslands?.All
            )
            R.string.Tuvalu -> caseInfo = arrayOf(
                caseData?.Tuvalu?.All,
                historicCases?.Tuvalu?.All,
                historicDeaths?.Tuvalu?.All
            )
            R.string.Uganda -> caseInfo = arrayOf(
                caseData?.Uganda?.All,
                historicCases?.Uganda?.All,
                historicDeaths?.Uganda?.All
            )
            R.string.Ukraine -> caseInfo = arrayOf(
                caseData?.Ukraine?.All,
                historicCases?.Ukraine?.All,
                historicDeaths?.Ukraine?.All
            )
            R.string.UnitedArabEmirates -> caseInfo = arrayOf(
                caseData?.UnitedArabEmirates?.All,
                historicCases?.UnitedArabEmirates?.All,
                historicDeaths?.UnitedArabEmirates?.All
            )
            R.string.UnitedKingdom -> caseInfo = arrayOf(
                caseData?.UnitedKingdom?.All,
                historicCases?.UnitedKingdom?.All,
                historicDeaths?.UnitedKingdom?.All
            )
            R.string.UnitedStates -> caseInfo = arrayOf(
                caseData?.UnitedStates?.All,
                historicCases?.UnitedStates?.All,
                historicDeaths?.UnitedStates?.All
            )
            //R.string.UnitedStatesMinorOutlyingIslands -> caseInfo = arrayOf(caseData?.UnitedStatesMinorOutlyingIslands?.All, historicCases?.UnitedStatesMinorOutlyingIslands?.All, historicDeaths?.UnitedStatesMinorOutlyingIslands?.All)
            R.string.Uruguay -> caseInfo = arrayOf(
                caseData?.Uruguay?.All,
                historicCases?.Uruguay?.All,
                historicDeaths?.Uruguay?.All
            )
            R.string.Uzbekistan -> caseInfo = arrayOf(
                caseData?.Uzbekistan?.All,
                historicCases?.Uzbekistan?.All,
                historicDeaths?.Uzbekistan?.All
            )
            R.string.Vanuatu -> caseInfo = arrayOf(
                caseData?.Vanuatu?.All,
                historicCases?.Vanuatu?.All,
                historicDeaths?.Vanuatu?.All
            )
            R.string.Venezuela -> caseInfo = arrayOf(
                caseData?.Venezuela?.All,
                historicCases?.Venezuela?.All,
                historicDeaths?.Venezuela?.All
            )
            R.string.Vietnam -> caseInfo = arrayOf(
                caseData?.Vietnam?.All,
                historicCases?.Vietnam?.All,
                historicDeaths?.Vietnam?.All
            )
            R.string.VirginIslands -> caseInfo = arrayOf(
                caseData?.VirginIslandsUK?.All,
                historicCases?.VirginIslandsUK?.All,
                historicDeaths?.VirginIslandsUK?.All
            )
            //R.string.USVirginIslands -> caseInfo = arrayOf(caseData?.VirginIslandsUS?.All, historicCases?.VirginIslandsUS?.All, historicDeaths?.VirginIslandsUS?.All)
            R.string.WallisandFutunaIslands -> caseInfo = arrayOf(
                caseData?.WallisAndFutunaIslands?.All,
                historicCases?.WallisAndFutunaIslands?.All,
                historicDeaths?.WallisAndFutunaIslands?.All
            )
            R.string.WesternSahara -> caseInfo = arrayOf(
                caseData?.WesternSahara?.All,
                historicCases?.WesternSahara?.All,
                historicDeaths?.WesternSahara?.All
            )
            R.string.Yemen -> caseInfo =
                arrayOf(caseData?.Yemen?.All, historicCases?.Yemen?.All, historicDeaths?.Yemen?.All)
            R.string.Zambia -> caseInfo = arrayOf(
                caseData?.Zambia?.All,
                historicCases?.Zambia?.All,
                historicDeaths?.Zambia?.All
            )
            R.string.Zimbabwe -> caseInfo = arrayOf(
                caseData?.Zimbabwe?.All,
                historicCases?.Zimbabwe?.All,
                historicDeaths?.Zimbabwe?.All
            )
            R.string.Palestine -> caseInfo = arrayOf(
                caseData?.Palestine?.All,
                historicCases?.Palestine?.All,
                historicDeaths?.Palestine?.All
            )



        }
        return caseInfo
    }

    //onStop newly fetched Data has to be inserted into database
    override fun onStop() {
        Log.i("DATA SAVED", "YES")
        super.onStop()
    }
}