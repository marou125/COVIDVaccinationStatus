package com.marou125.covidvaccinationstatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marou125.covidvaccinationstatus.database.Country
import com.marou125.covidvaccinationstatus.service.CovidService
import com.marou125.covidvaccinationstatus.service.VaccinationData
import com.marou125.covidvaccinationstatus.service.VaccinationTimeStamp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//TODO: Add sorting option
class CountryListActivity : AppCompatActivity() {

    val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    val viewModel by lazy {
        ViewModelProvider(this).get(CountryListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

        recyclerView.layoutManager = LinearLayoutManager(this)


        viewModel.countryListLiveData.observe(
            this,
            Observer { countries ->
                countries?.let {
                    updateUI()
                }
            }
        )

        //TODO: this resets the tint but also appears to remove the selection navigation
        findViewById<BottomNavigationView>(R.id.bottom_nav).itemIconTintList=null

        findViewById<Button>(R.id.sort_button).setOnClickListener {
            var mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT)
            viewModel.sortCountries()
            if(viewModel.sortedByName){
                mToast.setText(getString(R.string.sorted_by_name))
            } else {
                mToast.setText(getString(R.string.sorted_by_population))
            }
            updateUI()
            mToast.show()
        }


    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.ExitToast), Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    fun updateUI(){
            recyclerView.adapter = CountryListAdapter(viewModel.americas)
        }


}