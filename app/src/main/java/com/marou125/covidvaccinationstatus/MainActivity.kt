package com.marou125.covidvaccinationstatus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Executors

private const val BASE_URL_VACCINATION_DATA = "https://raw.githubusercontent.com/"


//TODO: Refactor this into another class
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val executor = Executors.newSingleThreadExecutor()


        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_VACCINATION_DATA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        executor.execute{

            val service = retrofit.create(CovidService::class.java)
            val call = service.getVaccinationData()
            val i = Intent(this@MainActivity, CountryListActivity::class.java)

            call.enqueue(object : Callback<Array<VaccinationData>> {
                override fun onResponse(
                    call: Call<Array<VaccinationData>>?,
                    response: Response<Array<VaccinationData>>?
                ) {
                    if(response?.code() == 200){
                        val countryArray = response.body()
                        CountryDataSingleton.fillList(countryArray)
                    } else {
                        Log.i("Response", "but not code 200")
                    }
                }

                override fun onFailure(call: Call<Array<VaccinationData>>?, t: Throwable?) {
                    Log.i("Failure", "No data fetched")

                }
            })

            val caseCall = service.getCaseData("https://covid-api.mmediagroup.fr/v1/cases")

            caseCall.enqueue(object : Callback<JsonCaseData>{
                override fun onResponse(call: Call<JsonCaseData>?, response: Response<JsonCaseData>?) {
                    val caseData = response?.body()
                    if(caseData != null){
                        CountryDataSingleton.fillCaseData(caseData)
                    } else {
                        Toast.makeText(this@MainActivity, "Error while trying to save case data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonCaseData>?, t: Throwable?) {
                    Log.i("CaseCall", "Error $t")
                }
            })

            val historyConfirmedCall = service.getHistoricalData("https://covid-api.mmediagroup.fr/v1/history?status=confirmed")

            historyConfirmedCall.enqueue(object : Callback<JsonCaseData>{
                override fun onResponse(call: Call<JsonCaseData>?, response: Response<JsonCaseData>?) {
                    val historicConfirmed = response?.body()
                    if(historicConfirmed != null){
                        CountryDataSingleton.fillHistoricConfirmed(historicConfirmed)
                    }
                }

                override fun onFailure(call: Call<JsonCaseData>?, t: Throwable?) {
                    Log.i("historicConfirmed", "Error $t")
                }
            })


            val historyDeathsCall = service.getHistoricalData("https://covid-api.mmediagroup.fr/v1/history?status=deaths")

            historyDeathsCall.enqueue(object : Callback<JsonCaseData>{
                override fun onResponse(call: Call<JsonCaseData>?, response: Response<JsonCaseData>?) {
                    val historicDeaths = response?.body()
                    if(historicDeaths != null){
                        CountryDataSingleton.fillHistoricDeaths(historicDeaths)
                    }
                }

                override fun onFailure(call: Call<JsonCaseData>?, t: Throwable?) {
                    Log.i("historicDeaths", "Error $t")
                }
            })

            startActivity(i)
        }
    }
}