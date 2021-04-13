package com.marou125.covidvaccinationstatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.service.CovidService
import com.marou125.covidvaccinationstatus.service.VaccinationData
import com.marou125.covidvaccinationstatus.service.VaccinationTimeStamp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryListActivity : AppCompatActivity() {

    val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

        val viewModel = ViewModelProvider(this).get(CountryListViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(this)


        viewModel.countryListLiveData.observe(
            this,
            Observer { countries ->
                countries?.let {
                    Log.i("TAG","Got countries ${countries.size}")
                    updateUI(countries)
                }
            }
        )


    }

        fun updateUI(countries: List<Country>){
            recyclerView.adapter = CountryListAdapter(countries)
        }


}