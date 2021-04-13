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

    //onStop newly fetched Data has to be inserted into database
    override fun onStop() {
        Log.i("DATA SAVED" , "YES")
        super.onStop()
    }
}